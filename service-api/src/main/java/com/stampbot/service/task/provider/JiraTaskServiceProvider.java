package com.stampbot.service.task.provider;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class JiraTaskServiceProvider implements TaskServiceProvider {

    @Override
    public List<String> validateIds(List<String> ids) throws Exception {
        PropertiesClient propertiesClient = new PropertiesClient();
        JiraOAuthClient jiraOAuthClient = new JiraOAuthClient(propertiesClient);
        ids.parallelStream().forEach( jiraId -> {
            try{
                new OAuthClient(propertiesClient, jiraOAuthClient).execute(Command.fromString("request"), Lists.newArrayList(jiraId));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return ids;
    }

}
