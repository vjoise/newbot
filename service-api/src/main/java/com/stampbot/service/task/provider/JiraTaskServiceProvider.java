package com.stampbot.service.task.provider;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
class JiraTaskServiceProvider implements TaskServiceProvider {

    private final PropertiesClient propertiesClient;
    private final JiraOAuthClient jiraOAuthClient;

    public JiraTaskServiceProvider() throws Exception {
        propertiesClient = new PropertiesClient();
        jiraOAuthClient = new JiraOAuthClient(propertiesClient);
    }

    @Override
    public List<String> validateIds(List<String> ids) {
        ids.parallelStream().forEach(jiraId -> {
            try {
                new OAuthClient(propertiesClient, jiraOAuthClient).execute(Command.fromString("request"), Lists.newArrayList(jiraId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ids;
    }

}
