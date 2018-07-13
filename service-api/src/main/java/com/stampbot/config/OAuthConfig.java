package com.stampbot.config;


import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.stampbot.workflow.service.task.provider.JiraOAuthGetAccessToken;
import com.stampbot.workflow.service.task.provider.JiraOAuthTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class OAuthConfig {

    @Autowired
    OAuthContextHolder contextHolder;

    @Autowired
    JiraOAuthTokenFactory jiraOAuthTokenFactory;

    @Bean
    public OAuthParameters getOAuthParameters() throws InvalidKeySpecException, NoSuchAlgorithmException {
        JiraOAuthGetAccessToken oAuthAccessToken = jiraOAuthTokenFactory.getJiraOAuthGetAccessToken(
                contextHolder.getAccessToken(), contextHolder.getSecret(), contextHolder.getConsumerKey(), contextHolder.getPrivateKey());
        oAuthAccessToken.verifier = contextHolder.getSecret();
        return oAuthAccessToken.createParameters();
    }

    @Bean
    public HttpRequestFactory getHttpRequestFactory(OAuthParameters parameters){
        return new NetHttpTransport().createRequestFactory(parameters);
    }
}
