package com.stampbot.service.task;

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

    public String createSubTask(String parentJira) throws Exception {
        return provider.createSubTask(parentJira);
    }

}
