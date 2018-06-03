package com.stampbot.service.task.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.ImmutableMap;
import com.stampbot.model.CreateIssueDto;
import com.stampbot.model.IssueResponse;
import com.stampbot.model.createIssueModel.Parent;
import com.stampbot.model.createMetaModel.ProjectMetaData;
import com.stampbot.model.issueModel.Fields;
import com.stampbot.model.issueModel.Issuetype;
import com.stampbot.model.issueModel.Project;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import static com.stampbot.service.task.provider.PropertiesClient.*;

public class OAuthClient {

    private final Map<Command, Function<List<String>, Optional<Exception>>> actionHandlers;

    private final PropertiesClient propertiesClient;
    private final JiraOAuthClient jiraOAuthClient;

    private final String metaApi = "rest/api/2/issue/createmeta";
    private final String newIssueApi = "rest/api/2/issue";
    private final String getIssueApi = "rest/api/latest/issue/";

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public OAuthClient(PropertiesClient propertiesClient, JiraOAuthClient jiraOAuthClient) {
        this.propertiesClient = propertiesClient;
        this.jiraOAuthClient = jiraOAuthClient;

        actionHandlers = ImmutableMap.<Command, Function<List<String>, Optional<Exception>>>builder()
                .put(Command.REQUEST_TOKEN, this::handleGetRequestTokenAction)
                .put(Command.ACCESS_TOKEN, this::handleGetAccessToken)
                .put(Command.REQUEST, this::handleGetRequest)
                .put(Command.CREATE_SUB_TASK, this::handleCreateSubTask)
                .build();
    }

    /**
     * Executes action (if found) with  given lists of arguments
     *
     * @param action
     * @param arguments
     */
    public void execute(Command action, List<String> arguments) {
        actionHandlers.getOrDefault(action, this::handleUnknownCommand)
                .apply(arguments)
                .ifPresent(Throwable::printStackTrace);
    }

    private Optional<Exception> handleUnknownCommand(List<String> arguments) {
        System.out.println("Command not supported. Only " + Command.names() + " are supported.");
        return Optional.empty();
    }

    /**
     * Gets request token and saves it to properties file
     *
     * @param arguments list of arguments: no arguments are needed here
     * @return
     */
    private Optional<Exception> handleGetRequestTokenAction(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();
        try {
            String requestToken = jiraOAuthClient.getAndAuthorizeTemporaryToken(properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            properties.put(REQUEST_TOKEN, requestToken);
            propertiesClient.savePropertiesToFile(properties);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    /**
     * Gets access token and saves it to properties file
     *
     * @param arguments list of arguments: first argument should be secert (verification code provided by JIRA after request token authorization)
     * @return
     */
    private Optional<Exception> handleGetAccessToken(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();
        String tmpToken = properties.get(REQUEST_TOKEN);
        String secret = arguments.get(0);

        try {
            String accessToken = jiraOAuthClient.getAccessToken(tmpToken, secret, properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            properties.put(ACCESS_TOKEN, accessToken);
            properties.put(SECRET, secret);
            propertiesClient.savePropertiesToFile(properties);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    private static HttpResponse postResponseFromUrl(OAuthParameters parameters, GenericUrl jiraUrl, HttpContent content) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
        HttpRequest request = requestFactory.buildPostRequest(jiraUrl, content);
        request.getHeaders().setContentType("application/json");
        return request.execute();
    }

    /**
     * Makes request to JIRA to provided url and prints response contect
     *
     * @param arguments list of arguments: first argument should be request url
     * @return
     */
    private Optional<Exception> handleGetRequest(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();
        String tmpToken = properties.get(ACCESS_TOKEN);
        String secret = properties.get(SECRET);
        String url = properties.get(JIRA_HOME) + "rest/api/latest/issue/" + arguments.get(0);
        propertiesClient.savePropertiesToFile(properties);

        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(tmpToken, secret, properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            HttpResponse response = getResponseFromUrl(parameters, new GenericUrl(url));
            parseResponse(response);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    private Optional<Exception> handleCreateSubTask(List<String> arguments) {
        Map<String, String> properties = propertiesClient.getPropertiesOrDefaults();
        String tmpToken = properties.get(ACCESS_TOKEN);
        String secret = properties.get(SECRET);
        String metaUrl = properties.get(JIRA_HOME) + metaApi;
        String getIssueUrl = properties.get(JIRA_HOME) + getIssueApi;
        String newIssueUrl = properties.get(JIRA_HOME) + newIssueApi;
        propertiesClient.savePropertiesToFile(properties);

        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(tmpToken, secret, properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            String parentJiraId = arguments.get(0);
            IssueResponse createSubTaskResponse = createSubTask(metaUrl, getIssueUrl, newIssueUrl, parameters, parentJiraId);
            if (createSubTaskResponse.getKey() != null) {
                System.out.println("Testing Sub-Task " + createSubTaskResponse.getKey() + " created on " + parentJiraId);
                this.setResponse(createSubTaskResponse.getKey());
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    private IssueResponse createSubTask(String metaUrl, String getIssueUrl, String newIssueUrl, OAuthParameters parameters, String jiraIssueKey) throws IOException {
        String projectId = getProjectIdFromIssue(parameters, getIssueUrl + "AUD-15", jiraIssueKey);

        String subTaskId = getSubTaskIdFromMeta(parameters, metaUrl, projectId);

        return createSubTaskResponse(parameters, newIssueUrl, jiraIssueKey, projectId, subTaskId);
    }

    private IssueResponse createSubTaskResponse(OAuthParameters parameters, String newIssueUrl, String jiraIssueKey, String projectId, String subTaskId) throws IOException {
        CreateIssueDto createIssueRequestBody = createSubTaskRequestBody(subTaskId, "Testing", projectId, jiraIssueKey);
        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(createIssueRequestBody);
        System.out.println(requestBody);

        HttpResponse createIssueHttpResponse = postResponseFromUrl(parameters, new GenericUrl(newIssueUrl),
                ByteArrayContent.fromString("application/json", requestBody));
        JSONObject issueJsonResponse = parseResponse(createIssueHttpResponse);

        return om.readValue(issueJsonResponse.toString(2), IssueResponse.class);
    }

    private String getProjectIdFromIssue(OAuthParameters parameters, String getIssueUrl, String jiraIssueKey) throws IOException {
        HttpResponse issueResponse = getResponseFromUrl(parameters, new GenericUrl(getIssueUrl));
        JSONObject issueJson = parseResponse(issueResponse);
        ObjectMapper om = new ObjectMapper();
        IssueResponse response = om.readValue(issueJson.toString(2), IssueResponse.class);

        return response.getFields().getProject().getId();
    }

    private String getSubTaskIdFromMeta(OAuthParameters parameters, String metaUrl, String projectId) throws IOException {
        HttpResponse metaResponse = getResponseFromUrl(parameters, new GenericUrl(metaUrl));
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

    /**
     * Authanticates to JIRA with given OAuthParameters and makes request to url
     *
     * @param parameters
     * @param jiraUrl
     * @return
     * @throws IOException
     */
    private static HttpResponse getResponseFromUrl(OAuthParameters parameters, GenericUrl jiraUrl) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
        HttpRequest request = requestFactory.buildGetRequest(jiraUrl);
        return request.execute();
    }

    /**
     * Prints response content
     * if response content is valid JSON it prints it in 'pretty' format
     *
     * @param response
     * @throws IOException
     */
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
}
