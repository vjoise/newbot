package com.stampbot.workflow.dao;


import com.google.api.client.http.HttpRequestFactory;
import com.stampbot.workflow.service.task.provider.JiraOAuthClient;
import com.stampbot.workflow.service.task.provider.OAuthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class OAuthDao {

    @Value("${jira.oauth.consumerKey}")
    private String consumerKey;
    @Value("${jira.oauth.privateKey}")
    private String privateKey;

    @Value("${jira.url.createMeta}")
    private String createMetaUrl;


    @Autowired
    JiraOAuthClient jiraOAuthClient;
    @Autowired
    HttpRequestFactory httpRequestFactory;

    public String getRequestToken() {
        try {
            OAuthContext.requestToken = jiraOAuthClient.getAndAuthorizeTemporaryToken(consumerKey, privateKey);
            return "Request Token Loaded : " + OAuthContext.requestToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "Operation Failed";
        }
    }

    public String getAccessToken(String secret) {
        try {
                String accessToken = jiraOAuthClient.getAccessToken(OAuthContext.requestToken, secret, consumerKey,privateKey);
                OAuthContext.secret = secret;
                OAuthContext.accessToken = accessToken;
                return "Access Token Loaded " + OAuthContext.accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "Operation Failed";
        }
    }



}
