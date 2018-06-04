package com.stampbot.service.workflow.handler;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.model.issueModel.Version;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.symphonyoss.client.events.SymEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DevQuestionWorkflowHandler implements WorkflowQuesionHandler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SymphonyService symphonyService;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowQuestionRepository questionRepository;

    @Override
    public void handle(Map<String, Object> context) {
        UserInput userInput = UserInput.class.cast(context.get("userInput"));
        userInput.setDetectedWorkflow("DEV_WORKFLOW");                                  //HARD-CODED
        WorkflowQuestionEntity byWorkflowName = questionRepository.findByWorkflowName(userInput.getDetectedWorkflow());
        byWorkflowName.setActionHandler(DevQuestionWorkflowHandler.class.getName());    //HARD-CODED
        SymEvent symEvent = SymEvent.class.cast(context.get("symEvent"));
        List<String> words = userInput.getWords().stream().map(UserInputWord::getWord).collect(Collectors.toList());
    }

    private void getIssuesForSprint(SymEvent symEvent, UserInput userInput) {
        if (toGetIssues(userInput)) {

        }
    }

    private boolean toGetIssues(UserInput userInput) {
        return !startReleasingForSprint(userInput).equals("");
    }

    private String startReleasingForSprint(UserInput userInput) {
        if (contains(userInput, "release") || contains(userInput, "version")) {
            List<Version> projectVersions = taskService.getProjectVersions();
            if (projectVersions.size() > 0) {
                List<String> versionNames = projectVersions.stream()
                        .map(Version::getName)
                        .collect(Collectors.toList());

                Optional<UserInputWord> releaseVersion = userInput.getWords()
                        .stream()
                        .filter(userInputWord -> versionNames.contains(userInputWord.getWord()))
                        .findFirst();
            }
            // get all sprint
            // contains any sprint
            // return sprint name

        }
        return "";
    }

    private boolean contains(UserInput userInput, String word) {
        return userInput.getInputSentence().contains(word);
    }
}
