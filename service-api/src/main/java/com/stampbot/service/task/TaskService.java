package com.stampbot.service.task;

import com.stampbot.dao.JiraTaskDao;
import com.stampbot.model.IssueResponse;
import com.stampbot.service.task.provider.TaskServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskService {

    @Autowired
    private TaskServiceProvider provider;

    public List<String> validateIds(List<String> input) throws Exception {
        return provider.validateIds(input);
    }

    @Autowired
    JiraTaskDao jiraTaskDao;

    public IssueResponse createSubTask(String jiraIssueKey) {
        return jiraTaskDao.createSubTask(jiraIssueKey);
    }
}
