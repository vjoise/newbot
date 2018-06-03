package com.stampbot.dao;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.stampbot.model.CreateIssueDto;
import com.stampbot.model.IssueResponse;
import com.stampbot.model.createIssueModel.Parent;
import com.stampbot.model.createMetaModel.ProjectMetaData;
import com.stampbot.model.issueModel.Fields;
import com.stampbot.model.issueModel.Issuetype;
import com.stampbot.model.issueModel.Project;
import com.stampbot.service.task.provider.JiraOAuthClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Scanner;

@Repository
public class JiraTaskDao {
    @Value("${jira.oauth.consumerKey}")
    private String consumerKey;
    @Value("${jira.oauth.privateKey}")
    private String privateKey;

    @Value("${jira.url.createMeta}")
    private String createMetaUrl;
    @Value("${jira.url.createIssue}")
    private String createIssueUrl;

    @Value("${jira.url.getIssue}")
    private String getIssueUrl;
    @Autowired
    JiraOAuthClient jiraOAuthClient;

    @Autowired
    HttpRequestFactory httpRequestFactory;


    public IssueResponse createSubTask(String jiraIssueKey) {
        String projectId = null;
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
        CreateIssueDto createIssueRequestBody = createSubTaskRequestBody(subTaskId, "Testing", projectId, jiraIssueKey);
        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(createIssueRequestBody);
        System.out.println(requestBody);

        HttpResponse createIssueHttpResponse = postResponseFromUrl(createIssueUrl,
                ByteArrayContent.fromString("application/json", requestBody));
        JSONObject issueJsonResponse = parseResponse(createIssueHttpResponse);

        return om.readValue(issueJsonResponse.toString(2), IssueResponse.class);
    }

    private String getProjectIdFromIssue(String jiraIssueKey) throws IOException {
        HttpResponse issueResponse = getResponseFromUrl(getIssueUrl + jiraIssueKey.toUpperCase());
        JSONObject issueJson = parseResponse(issueResponse);
        ObjectMapper om = new ObjectMapper();
        IssueResponse response = om.readValue(issueJson.toString(2), IssueResponse.class);

        return response.getFields().getProject().getId();
    }


    private String getSubTaskIdFromMeta(String projectId) throws IOException {
        HttpResponse metaResponse = getResponseFromUrl(createMetaUrl);
        JSONObject metaJson = parseResponse(metaResponse);
        ObjectMapper om = new ObjectMapper();
        ProjectMetaData meta = om.readValue(metaJson.toString(2), ProjectMetaData.class);
        Project theProject = meta.getProjects().stream().filter(project -> project.getId().equals(projectId)).findFirst().get();
        return theProject.getIssuetypes().stream().filter(issuetype -> issuetype.isSubtask()).findFirst().get().getId();
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

//    helper
    private JSONObject parseResponse(HttpResponse response) throws IOException {
        Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println(jsonObj.toString(2));
            return jsonObj;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new JSONObject();
    }

    private HttpResponse postResponseFromUrl(String url,HttpContent content) throws IOException {
        HttpRequest request = httpRequestFactory.buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setContentType("application/json");
        return request.execute();
    }

    private HttpResponse getResponseFromUrl(String url) throws IOException {
        HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(url));
        return request.execute();
    }
}
