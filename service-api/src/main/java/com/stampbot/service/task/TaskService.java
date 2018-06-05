package com.stampbot.service.task;

import com.stampbot.dao.JiraTaskDao;
import com.stampbot.model.SprintsResponse;
import com.stampbot.model.boardModel.BoardsResponse;
import com.stampbot.model.editMetaModel.EditMeta;
import com.stampbot.model.issueModel.IssueResponse;
import com.stampbot.model.issueModel.Version;
import com.stampbot.model.transitionModel.TransitionFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskService {

    @Autowired
    JiraTaskDao jiraTaskDao;

    public IssueResponse createSubTask(String jiraIssueKey) {
        return jiraTaskDao.createSubTask(jiraIssueKey.toUpperCase());
    }

    public List<Version> getProjectVersions() {
        return jiraTaskDao.getProjectVersions();
    }

    public BoardsResponse getBoards() {
        return jiraTaskDao.getAllBoards();
    }

    public int getBoardId() {
        return jiraTaskDao.getBoardId();
    }

    public List<String> validateIds(List<String> input) {
        return jiraTaskDao.validateIds(input.stream().map(String::toUpperCase).collect(Collectors.toList()));
    }

    public SprintsResponse getSprints(int boardId) {
        return jiraTaskDao.getAllSprints(boardId);
    }

    public int getActiveSprintId() {
        return jiraTaskDao.getActiveSprintId();
    }

    public int getMatchingNameSprintId(String sprintName) {
        return jiraTaskDao.getMatchingNameSprintId(sprintName);
    }

    public List<IssueResponse> getIssuesForSprintName(String sprintName) {
        return jiraTaskDao.getIssuesForSprintName(sprintName);
    }

    public EditMeta getEditMeta(String issueIdOrKey) {
        return jiraTaskDao.getEditMeta(issueIdOrKey);
    }

    public TransitionFields getTransitionFields(String issueIdOrKey) {
        return jiraTaskDao.getTransitionFields(issueIdOrKey);
    }

    public String completeTesting(String issueKey) {
        return jiraTaskDao.completeTesting(issueKey);
    }

    public String completeTesting(String issueKey, String newStatus, String newComment, boolean assignToReporter) {
        return jiraTaskDao.completeTesting(issueKey, newStatus, newComment, assignToReporter);
    }

    public String editIssue(String issueKey, String newComment, boolean assignToReporter) {
        return jiraTaskDao.editIssue(issueKey, newComment, assignToReporter);
    }
}