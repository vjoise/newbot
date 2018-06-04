package com.stampbot.service.workflow.handler;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.entity.UserWorkflowLogEntity;
import com.stampbot.model.Sprint;
import com.stampbot.model.issueModel.IssueResponse;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.task.TaskService;
import com.stampbot.service.workflow.UserWorkflowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.symphonyoss.client.events.SymEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.stampbot.common.Utils.trySafe;

public class DevQuestionWorkflowHandler implements WorkflowQuesionHandler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SymphonyService symphonyService;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowQuestionRepository questionRepository;

    @Autowired
    private UserWorkflowStore userWorkflowStore;

    private String sprintName;

    @Override
    public void handle(Map<String, Object> context) {
        UserInput userInput = UserInput.class.cast(context.get("userInput"));
        userInput.setDetectedWorkflow("DEV_WORKFLOW");  //HARD-CODED
        boolean no = userInput.getWords().stream().anyMatch(word -> word.getWord().equalsIgnoreCase("no"));
        SymEvent symEvent = SymEvent.class.cast(context.get("symEvent"));
        if (no) {
            trySafe(() -> symphonyService.sendMessage(symEvent, "Thank you for using KAKI, have a Productive Day!"), false);
            return;
        }
        UserInput previousUserInput = mockPreviousInput(userInput);
        printIssuesForSprint(previousUserInput, symEvent);
//        printIssuesForSprint(userInput, symEvent);
    }

    private UserInput mockPreviousInput(UserInput userInput) {
        UserWorkflowLogEntity previousWorkflowLogEntity = userWorkflowStore.findQuestionWithNextQuestionId(userInput.getQuestionEntity().getId());
        List<String> previousWords = Arrays.asList(previousWorkflowLogEntity.getInputText().split(" "));
        List<UserInputWord> userInputWords = previousWords.stream().
                map(word -> new UserInputWord(word, "", "")).collect(Collectors.toList());
        UserInput previousUserInput = new UserInput();
        previousUserInput.setWords(userInputWords);
        return previousUserInput;
    }

    private void printIssuesForSprint(UserInput userInput, SymEvent symEvent) {
        List<IssueResponse> issuesForSprint = getIssuesForSprint(symEvent, userInput);
        if (issuesForSprint != null && issuesForSprint.size() > 0) {
            trySafe(() -> {
                String message = "Found " + issuesForSprint.size() + " issues in this sprint :";
                symphonyService.sendMessage(symEvent, message);
                printIssues(symEvent, issuesForSprint);
                symphonyService.sendMessage(symEvent, message);
            }, false);
        }
    }

    private void printIssues(SymEvent symEvent, List<IssueResponse> issuesForSprint) {
        issuesForSprint.forEach(issue -> trySafe(() -> {
            String message = issue.getKey() + " : "
                    + issue.getFields().getStatus() + " : "
                    + issue.getFields().getSummary();
            symphonyService.sendMessage(symEvent, message);
        }, false));
    }

    private List<IssueResponse> getIssuesForSprint(SymEvent symEvent, UserInput userInput) {
        if (toGetIssues(userInput)) {
            trySafe(() -> symphonyService.sendMessage(symEvent, "Getting issues for sprint " + this.sprintName), false);
            List<IssueResponse> sprints = taskService.getIssuesForSprintName(this.sprintName);
            if (sprints.size() > 0) {
                return sprints;
            }
        }
        return null;
    }

    private boolean toGetIssues(UserInput userInput) {
        this.sprintName = foundSprintName(userInput);
        return !this.sprintName.equals("");
    }

    private String foundSprintName(UserInput userInput) {
        if (contains(userInput, "release") || contains(userInput, "version")) {
            List<Sprint> allSprints = taskService.getSprints(taskService.getBoardId()).getValues();
            if (allSprints.size() > 0) {
                List<String> allSprintNames = allSprints.stream()
                        .map(Sprint::getName)
                        .collect(Collectors.toList());
                List<String> foundQuotedString = findQuotedString(userInput);
                if (foundQuotedString.size() > 0) {
                    List<String> sprintNames = foundQuotedString.stream()
                            .map(string -> string.replaceAll("\"", ""))
                            .filter(sprintName -> taskService.getMatchingNameSprintId(sprintName) >= 0)
                            .collect(Collectors.toList());
                    if (sprintNames.size() > 0) {
                        return sprintNames.get(0);
                    }
                }
            }
        }
        return "";
    }

    private List<String> findQuotedString(UserInput userInput) {
        Pattern pattern = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1");
        Matcher matcher = pattern.matcher(userInput.getInputSentence());
        boolean matches = matcher.matches();
        List<String> foundSprintNames = new ArrayList<>();
        while (matcher.find()) {
            foundSprintNames.add(matcher.group(1));
        }
        return foundSprintNames;
    }

    private boolean contains(UserInput userInput, String word) {
        return userInput.getInputSentence().contains(word);
    }
}
