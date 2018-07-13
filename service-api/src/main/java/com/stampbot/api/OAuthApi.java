package com.stampbot.api;

import com.stampbot.workflow.model.CompleteTestParams;
import com.stampbot.workflow.model.boardModel.BoardsResponse;
import com.stampbot.workflow.model.editMetaModel.EditMeta;
import com.stampbot.workflow.model.issueModel.IssueResponse;
import com.stampbot.workflow.model.issueModel.SprintsResponse;
import com.stampbot.workflow.model.issueModel.Version;
import com.stampbot.workflow.model.transitionModel.TransitionFields;
import com.stampbot.workflow.service.task.JiraTaskService;
import com.stampbot.workflow.service.task.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1")
public class OAuthApi {
    @Autowired
    OAuthService oAuthService;
    @Autowired
    JiraTaskService jiraJiraTaskService;

    @GetMapping("/getRequestToken")
    public String getRequestToken() {
        return oAuthService.getRequestToken();
    }

    @GetMapping("/getAccessToken/{secret}")
    public String getAccessToken(@PathVariable("secret") String secret) {
        return oAuthService.getAccessToken(secret);
    }

    @GetMapping(value = "/createSubTask/{jiraIssueKey}", produces = "application/json; charset=UTF-8")
    public IssueResponse createSubTask(@PathVariable String jiraIssueKey) {
        jiraIssueKey = jiraIssueKey.replaceAll("%20", " ");
        return jiraJiraTaskService.createSubTask(jiraIssueKey);
    }

    @GetMapping("/getProjectVersions")
    public List<Version> getProjectVersions() {
        return jiraJiraTaskService.getProjectVersions();
    }

    @GetMapping("/getBoards")
    public BoardsResponse getBoards() {
        return jiraJiraTaskService.getBoards();
    }

    @GetMapping("/getBoardId")
    public int getBoardId() {
        return jiraJiraTaskService.getBoardId();
    }

    @GetMapping("/getSprints")
    public SprintsResponse getSprints() {
        return jiraJiraTaskService.getSprints(getBoardId());
    }

    @GetMapping("/getActiveSprintId")
    public int getActiveSprintId() {
        return jiraJiraTaskService.getActiveSprintId();
    }

    @GetMapping("/getMatchingNameSprintId/{sprintName}")
    public int getMatchingNameSprintId(@PathVariable String sprintName) {
        sprintName = sprintName.replaceAll("%20", " ");
        return jiraJiraTaskService.getMatchingNameSprintId(sprintName);
    }

    @GetMapping("/getIssuesForSprintName/{sprintName}")
    public List<IssueResponse> getIssuesForSprintName(@PathVariable String sprintName) {
        sprintName = sprintName.replaceAll("%20", " ");
        return jiraJiraTaskService.getIssuesForSprintName(sprintName);
    }

    @GetMapping("/editMeta/{issueIdOrKey}")
    public EditMeta getEditMeta(@PathVariable String issueIdOrKey) {
        issueIdOrKey = issueIdOrKey.replaceAll("%20", " ");
        return jiraJiraTaskService.getEditMeta(issueIdOrKey);
    }

    @GetMapping("/getTransitionFields/{issueIdOrKey}")
    public TransitionFields getTransitionFields(@PathVariable String issueIdOrKey) {
        issueIdOrKey = issueIdOrKey.replaceAll("%20", " ");
        return jiraJiraTaskService.getTransitionFields(issueIdOrKey);
    }

    @PutMapping("/updateStatus/{issueKey}/{newStatus}")
    public ResponseEntity<String> updateStatus(@PathVariable String issueKey, @PathVariable String newStatus) {
        issueKey = issueKey.replaceAll("%20", " ");
        newStatus = newStatus.replaceAll("%20", " ");
        String response = jiraJiraTaskService.moveToNewStatus(issueKey, newStatus);
        if (!response.equals("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
    }

    @PostMapping(path = "/completeTesting", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> postCompleteTesting(
            @RequestBody CompleteTestParams requestJsonObject
    ) {
        String issueKey = requestJsonObject.getIssueKey();
        String newStatus = requestJsonObject.getStatus();
        String newComment = requestJsonObject.getComment();
        boolean assignToReporter = requestJsonObject.isAssignToReporter();

        String response = jiraJiraTaskService.completeTesting(issueKey, newStatus, newComment, assignToReporter);
        if (!response.equals("SUCCESS")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
    }

    @PutMapping(path = "/editIssue", consumes = "application/json", produces = "application/json")
    public String editIssue(
            @RequestBody CompleteTestParams requestJsonObject
    ) {
        String issueKey = requestJsonObject.getIssueKey();
        String newComment = requestJsonObject.getComment();
        boolean assignToReporter = requestJsonObject.isAssignToReporter();

        return jiraJiraTaskService.assignTo(issueKey, assignToReporter);
    }

    @GetMapping("/user/{userId}/issues")
    public List<IssueResponse> getIssuesAssignedToUser(@PathVariable String userId) {
        return jiraJiraTaskService.getIssuesAssignedToUser(userId);
    }
}
