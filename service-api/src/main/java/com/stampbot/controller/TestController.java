package com.stampbot.controller;

import com.stampbot.service.task.provider.TaskServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    final
    TaskServiceProvider provider;

    @Autowired
    public TestController(TaskServiceProvider provider) {
        this.provider = provider;
    }

    @PostMapping("/createSubTask/{parentJiraId}")
    public String createSubTask(@PathVariable String parentJiraId) throws Exception {
        provider.createSubTask(parentJiraId);
        return "check console";
    }

}
