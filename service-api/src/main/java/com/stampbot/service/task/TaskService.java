package com.stampbot.service.task;

import com.stampbot.dao.JiraTaskDao;
import com.stampbot.model.IssueResponse;
import com.stampbot.model.issueModel.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskService {

    @Autowired
    JiraTaskDao jiraTaskDao;
    private List<Version> projectVersions;

    public IssueResponse createSubTask(String jiraIssueKey) {
        return jiraTaskDao.createSubTask(jiraIssueKey.toUpperCase());
    }

    public List<String> validateIds(List<String> input) {
        return jiraTaskDao.validateIds(input.stream().map(String::toUpperCase).collect(Collectors.toList()));
    }

    public List<Version> getProjectVersions() {
        return projectVersions;
    }
}
