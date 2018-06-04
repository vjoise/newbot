package com.stampbot.api;

import com.stampbot.model.SprintsResponse;
import com.stampbot.model.boardModel.BoardsResponse;
import com.stampbot.model.issueModel.IssueResponse;
import com.stampbot.model.issueModel.Version;
import com.stampbot.service.task.OAuthService;
import com.stampbot.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/1")
public class OAuthApi {
    @Autowired
    OAuthService oAuthService;
    @Autowired
    TaskService jiraTaskService;

    @GetMapping("/getRequestToken")
    public String getRequestToken() {
        return oAuthService.getRequestToken();
    }

    @GetMapping("/getAccessToken/{secret}")
    public String getAccessToken(@PathVariable("secret") String secret) {
        return oAuthService.getAccessToken(secret);
    }

    @GetMapping(value = "/createSubTask/{jiraIssueId}", produces = "application/json; charset=UTF-8")
    public IssueResponse createSubTask(@PathVariable String jiraIssueId) {
        return jiraTaskService.createSubTask(jiraIssueId);
    }

    @GetMapping("/getProjectVersions")
    public List<Version> getProjectVersions() {
        return jiraTaskService.getProjectVersions();
    }

    @GetMapping("/getBoards")
    public BoardsResponse getBoards() {
        return jiraTaskService.getBoards();
    }

    @GetMapping("/getBoardId")
    public int getBoardId() {
        return jiraTaskService.getBoardId();
    }

    @GetMapping("/getSprints")
    public SprintsResponse getSprints() {
        return jiraTaskService.getSprints(getBoardId());
    }

    @GetMapping("/getActiveSprintId")
    public int getActiveSprintId() {
        return jiraTaskService.getActiveSprintId();
    }

    @GetMapping("/getMatchingNameSprintId/{sprintName}")
    public int getMatchingNameSprintId(@PathVariable String sprintName) {
        sprintName = sprintName.replaceAll("%20", " ");
        return jiraTaskService.getMatchingNameSprintId(sprintName);
    }

    @GetMapping("/getIssuesForSprintName/{sprintName}")
    public List<IssueResponse> getIssuesForSprintName(@PathVariable String sprintName) {
        sprintName = sprintName.replaceAll("%20", " ");
        return jiraTaskService.getIssuesForSprintName(sprintName);
    }
}
