package com.stampbot.dao;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpResponse;
import com.stampbot.common.HttpUtils;
import com.stampbot.model.IssuesResponse;
import com.stampbot.model.Sprint;
import com.stampbot.model.SprintsResponse;
import com.stampbot.model.boardModel.Board;
import com.stampbot.model.boardModel.BoardsResponse;
import com.stampbot.model.createIssueModel.CreateIssueDto;
import com.stampbot.model.createIssueModel.Parent;
import com.stampbot.model.createMetaModel.ProjectMetaData;
import com.stampbot.model.issueModel.*;
import com.stampbot.service.task.provider.JiraOAuthClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stampbot.common.HttpUtils.parseResponse;

@Repository
public class JiraTaskDao {
    ///////////// AUTH /////////////
    @Value("${jira.oauth.consumerKey}")
    private String consumerKey;
    @Value("${jira.oauth.privateKey}")
    private String privateKey;

    ///////////// URLs /////////////
    @Value("${jira.url.createMeta}")
    private String createMetaUrl;
    @Value("${jira.url.createIssue}")
    private String createIssueUrl;
    @Value("${jira.url.getIssue}")
    private String getIssueUrl;
    @Value("${jira.url.getVersions}")
    private String getVersionsUrl;
    @Value("${jira.url.getBoards}")
    private String getBoardsUrl;
    @Value("${jira.url.getSprints}")
    private String getSprintsUrl;
    @Value("${jira.url.getIssuesForSprint}")
    private String getIssuesForSprintUrl;

    ///////////// PROJ /////////////
    @Value("${jira.project.id}")
    private String configProjectId;
    @Value("${jira.project.key}")
    private String configProjectKey;

    @Autowired
    JiraOAuthClient jiraOAuthClient;

    @Autowired
    HttpUtils httpUtils;

    private String currentProjectId;
    private Project currentProject;
    private Board currentBoard;

    public List<String> validateIds(List<String> input) {
        return input.stream()
                .map(word -> {
                    try {
                        IssueResponse response = getIssueResponse(word);
                        return response != null ? response.getKey() : "";
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "";
                    }
                })
                .filter(word -> !word.equals(""))
                .collect(Collectors.toList());
    }

    public IssueResponse createSubTask(String jiraIssueKey) {
        String projectId;
        try {
            projectId = getProjectIdFromIssue(jiraIssueKey);

            String subTaskId = getSubTaskIdFromMeta(projectId);

            return createSubTaskResponse(jiraIssueKey, projectId, subTaskId);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private IssueResponse createSubTaskResponse(String jiraIssueKey, String projectId, String subTaskId) throws IOException {
        CreateIssueDto createIssueRequestBody = createSubTaskRequestBody(subTaskId, "Testing - " + jiraIssueKey, projectId, jiraIssueKey);
        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(createIssueRequestBody);
        System.out.println(requestBody);

        HttpResponse createIssueHttpResponse = httpUtils.postResponseFromUrl(createIssueUrl,
                ByteArrayContent.fromString("application/json", requestBody));
        JSONObject issueJsonResponse = parseResponse(createIssueHttpResponse);

        return om.readValue(issueJsonResponse.toString(2), IssueResponse.class);
    }

    private String getProjectIdFromIssue(String jiraIssueKey) throws IOException {
        IssueResponse response = getIssueResponse(jiraIssueKey);

        this.currentProject = response.getFields().getProject();
        this.currentProjectId = response.getFields().getProject().getId();

        return response.getFields().getProject().getId();
    }

    private IssueResponse getIssueResponse(String jiraIssueKey) throws IOException {
        HttpResponse issueResponse = httpUtils.getResponseFromUrl(getIssueUrl + jiraIssueKey);
        JSONObject issueJson = parseResponse(issueResponse);
        ObjectMapper om = new ObjectMapper();
        return om.readValue(issueJson.toString(2), IssueResponse.class);
    }

    private String getSubTaskIdFromMeta(String projectId) throws IOException {
        HttpResponse metaResponse = httpUtils.getResponseFromUrl(createMetaUrl);
        JSONObject metaJson = parseResponse(metaResponse);
        ObjectMapper om = new ObjectMapper();
        ProjectMetaData meta = om.readValue(metaJson.toString(2), ProjectMetaData.class);
        Project theProject = meta.getProjects().stream().filter(project -> project.getId().equals(projectId)).findFirst().get();
        return theProject.getIssuetypes().stream().filter(Issuetype::isSubtask).findFirst().get().getId();
    }

    private CreateIssueDto createSubTaskRequestBody(String subTaskId, String subTaskSummary, String projectId, String parentJiraKey) {
        try {
            Project project = new Project();
            project.setId(projectId);

            Issuetype issuetype = new Issuetype();
            issuetype.setId(subTaskId);

            Parent parent = new Parent();
            parent.setKey(parentJiraKey);

            Fields field = new Fields();
            field.setSummary(subTaskSummary);
            field.setProject(project);
            field.setIssuetype(issuetype);
            field.setParent(parent);

            CreateIssueDto createIssueDto = new CreateIssueDto();
            createIssueDto.setFields(field);

            return createIssueDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Version> getProjectVersions() {
        configureCurrentProjectId();
        HttpResponse versionsResponse;
        try {
            versionsResponse = httpUtils.getResponseFromUrl(getVersionsUrl.replace("{projectId}", this.currentProjectId));
            JSONArray versionsJson = HttpUtils.parseResponses(versionsResponse);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(versionsJson.toString(2), new TypeReference<List<Version>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BoardsResponse getAllBoards() {
        HttpResponse boardsResponse;
        try {
            boardsResponse = httpUtils.getResponseFromUrl(getBoardsUrl);
            JSONObject issueJson = parseResponse(boardsResponse);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(issueJson.toString(2), BoardsResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBoardId() {
        if (this.currentBoard == null) {
            Board getBoardForCurrentProject = getBoardForCurrentProject();

            if (getBoardForCurrentProject == null) {
                return -1;
            }
        }
        return this.currentBoard.getId();
    }

    private Board getBoardForCurrentProject() {
        if (this.currentBoard == null) {
            configureCurrentProjectId();

            List<Board> allBoards = getAllBoards().getValues();
            Optional<Board> currentBoard = allBoards.stream().filter(board -> board.getLocation().getProjectId().equals(this.currentProjectId)).findFirst();
            this.currentBoard = currentBoard.orElse(null);
        }
        return this.currentBoard;
    }

    private void configureCurrentProjectId() {
        if (this.currentProjectId == null && this.configProjectId != null) {
            this.currentProjectId = this.configProjectId;
        }
    }

    public SprintsResponse getAllSprints(int boardId) {
        HttpResponse sprintsResponse;
        try {
            sprintsResponse = httpUtils.getResponseFromUrl(getSprintsUrl.replace("{boardId}", String.valueOf(boardId)));
            JSONObject issueJson = parseResponse(sprintsResponse);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(issueJson.toString(2), SprintsResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getActiveSprintId() {
        Sprint getActiveSprint = getActiveSprint();
        if (getActiveSprint != null) {
            return getActiveSprint.getId();
        }
        return -1;
    }

    private Sprint getActiveSprint() {
        List<Sprint> allSprints = getAllSprints(getBoardId()).getValues();
        Optional<Sprint> matchingSprint = allSprints.stream().filter(sprint -> sprint.getState().equals("active")).findFirst();
        return matchingSprint.orElse(null);
    }

    public int getMatchingNameSprintId(String sprintName) {
        Sprint getMatchingNameSprint = getMatchingNameSprint(sprintName);
        if (getMatchingNameSprint == null) {
            return -1;
        }
        return getMatchingNameSprint.getId();
    }

    private Sprint getMatchingNameSprint(String sprintName) {
        List<Sprint> allSprints = getAllSprints(getBoardId()).getValues();
        Optional<Sprint> matchingSprint = allSprints.stream().filter(sprint -> sprint.getName().equals(sprintName)).findFirst();
        return matchingSprint.orElse(null);
    }

    public List<IssueResponse> getIssuesForSprintName(String sprintName) {
        int sprintId = getMatchingNameSprintId(sprintName);
        int boardId = getBoardId();
        IssuesResponse issuesResponse = getIssuesForSprintName(sprintId, boardId);
        if (issuesResponse != null && issuesResponse.getIssues().size() != 0) {
            return issuesResponse.getIssues();
        }
        return null;
    }

    private IssuesResponse getIssuesForSprintName(int sprintId, int boardId) {
        HttpResponse issuesResponse;
        try {
            issuesResponse = httpUtils.getResponseFromUrl(getIssuesForSprintUrl
                    .replace("{boardId}", String.valueOf(boardId))
                    .replace("{sprintId}", String.valueOf(sprintId)));
            JSONObject issuesJson = parseResponse(issuesResponse);
            ObjectMapper om = new ObjectMapper();
            return om.readValue(issuesJson.toString(2), IssuesResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
