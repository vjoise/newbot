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
