package com.stampbot.api;

import com.stampbot.model.IssueResponse;
import com.stampbot.service.task.OAuthService;
import com.stampbot.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1")
public class OAuthApi {
    @Autowired
    OAuthService oAuthService;
    @Autowired
    TaskService jiraTaskService;
    @GetMapping("/getRequestToken")
    public String getRequestToken(){
        return oAuthService.getRequestToken();
    }

    @GetMapping("/getAccessToken/{secret}")
    public String getAccessToken(@PathVariable("secret") String secret){
        return oAuthService.getAccessToken(secret);
    }

    @GetMapping(value = "/createSubTask/{jiraIssueId}",produces = "application/json; charset=UTF-8")
    public IssueResponse createSubTask(@PathVariable String jiraIssueId){
        return jiraTaskService.createSubTask(jiraIssueId);
    }
}
