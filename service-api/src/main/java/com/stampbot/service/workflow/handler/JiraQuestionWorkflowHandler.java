package com.stampbot.service.workflow.handler;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.model.IssueResponse;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.task.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.events.SymEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stampbot.common.Utils.trySafe;

@Component
@Slf4j
public class JiraQuestionWorkflowHandler implements WorkflowQuesionHandler {

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
		WorkflowQuestionEntity byWorkflowName = questionRepository.findByWorkflowName(userInput.getDetectedWorkflow());
		SymEvent symEvent = SymEvent.class.cast(context.get("symEvent"));
		List<String> ids = userInput.getWords().stream().map(UserInputWord::getWord).collect(Collectors.toList());
		trySafe(() -> {
			List<String> strings = taskService.validateIds(ids);
			log.info("Valid ones :: " + strings);
		}, true);
		createSubTask(symEvent, userInput);
	}

	private boolean toCreateSubTask(UserInput userInput) {
		return !startTestingForJira(userInput).equals("");
	}

	private String startTestingForJira(UserInput userInput) {
		if (userInput.getInputSentence().contains("test") && userInput.getInputSentence().contains("-")) {
			Optional<UserInputWord> jiraKey = userInput.getWords()
					.stream()
					.filter(userInputWord -> userInputWord.getWord().matches("((([a-zA-Z]{1,10})-)*[a-zA-Z]+-\\d+)"))
					.findFirst();
			if (jiraKey.isPresent()) {
				return jiraKey.get().getWord();
			} else {
				return "";
			}
		}
		return "";
	}

	private void createSubTask(SymEvent symEvent, UserInput userInput) {
		if (toCreateSubTask(userInput)) {
			String parentJiraKey = startTestingForJira(userInput);
			try {
				IssueResponse issueResponse = taskService.createSubTask(parentJiraKey);
				if (issueResponse != null && issueResponse.getKey() != null) {
					symphonyService.sendMessage(symEvent, "Testing Sub-Task " + issueResponse.getKey() + " created for " + parentJiraKey + ".");
				} else {
					throw new Exception("Invalid Response");
				}
			} catch (Exception e) {
				e.printStackTrace();
				trySafe(() -> {
					symphonyService.sendMessage(symEvent, "Sub-Task could not be created for " + parentJiraKey + ": " + e.getMessage());
				}, true);
			}
		}
	}
}
