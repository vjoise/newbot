package com.stampbot.service.workflow.handler;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.domain.UserIntent;
import com.stampbot.entity.ActionItemEntity;
import com.stampbot.entity.UserWorkflowLogEntity;
import com.stampbot.model.issueModel.IssueResponse;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.action.ActionItemService;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.task.TaskService;
import com.stampbot.service.workflow.UserWorkflowStore;
import com.stampbot.util.UserUtil;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.symphonyoss.client.events.SymEvent;

import java.util.Arrays;
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

	@Autowired
	private UserWorkflowStore userWorkflowStore;

	@Autowired
	private ActionItemService actionItemService;

	@Override
	public void handle(Map<String, Object> context) {
		UserInput userInput = UserInput.class.cast(context.get("userInput"));
		boolean no = userInput.getWords().stream().anyMatch(word -> word.getWord().equalsIgnoreCase("no"));
		SymEvent symEvent = SymEvent.class.cast(context.get("symEvent"));
		if (no) {
			trySafe(() -> {
				symphonyService.sendMessage(symEvent, "Thank you for using KAKI, have a Productive Day!");
			}, false);
			return;
		}
		UserWorkflowLogEntity previousWorkflowLogEntity = userWorkflowStore.findQuestionWithNextQuestionId(userInput.getQuestionEntity().getId()
				, userInput.getQuestionEntity().getWorkflowEntity().getId());
		List<String> words = Arrays.asList(previousWorkflowLogEntity.getInputText().split(" "));
		List<String> ids = words.stream().filter(id ->
				id.matches("((([a-zA-Z]{1,10})-)*[a-zA-Z]+-\\d+)")).collect(Collectors.toList());
		List<String> strings;
		try {
			strings = taskService.validateIds(ids);
			log.info("Valid ones :: " + strings);
		} catch (Exception e) {
			sendErrorToUser(symEvent, userInput.getWords().stream().map(UserInputWord::getWord).collect(Collectors.toList()));
			return;
		}
		if (CollectionUtils.isEmpty(strings)) {
			sendErrorToUser(symEvent, userInput.getWords().stream().map(UserInputWord::getWord).collect(Collectors.toList()));
			return;
		}
		String userMentionIdsList = previousWorkflowLogEntity.getUserMentionIdsList();
		List<String> userNameMentions = Arrays.stream(userMentionIdsList.split(","))
				.map(id -> UserUtil.getUser(Long.parseLong(id)).getDisplayName())
				.collect(Collectors.toList());
		trySafe(() -> {
			symphonyService.sendMessage(symEvent, "Please wait, while I create action items on Jira for the user(s) :: "
					+ userNameMentions);
		}, false);
		List<ActionItemEntity> actionItemEntities = Lists.newArrayList();
		Arrays.asList(userMentionIdsList.split(",")).forEach(userId -> {
			log.info("Persisting Workflow Action Items for user..." + userId);
			strings.forEach(task -> {
				ActionItemEntity entity = new ActionItemEntity();
				entity.setAction("User Testing");
				entity.setStatus("NOT_STARTED");
				entity.setUserId(Long.parseLong(userId));
				entity.setResource(task);
				actionItemEntities.add(entity);
			});
		});
		/*Persist actionItems here.*/
		actionItemService.createActionItems(actionItemEntities);
		userInput.setInputSentence(previousWorkflowLogEntity.getInputText());
		UserIntent intent = UserUtil.getIntent(userInput.getUserId());
		intent.getUserInputList().sort((left, right) -> {
			if (left.getTimeStamp().isBefore(right.getTimeStamp())) {
				return -1;
			}
			return 1;
		});
		UserInput tempInput = intent.getUserInputList().get(0);
		createSubTask(symEvent, tempInput);
		Arrays.asList(userMentionIdsList.split(",")).forEach(userId -> {
			trySafe(() -> {
				symphonyService.sendMessage(UserUtil.getUser(Long.parseLong(userId)), "Please wait, while I create action items on Jira for the user(s) :: "
						+ userNameMentions);
			}, false);
		});
		context.put("workflowEnded", true);
	}

	private void sendErrorToUser(SymEvent symEvent, List<String> strings) {
		log.info("The Jira Id(s) seems to be invalid! :: " + strings);
		List<String> finalStrings = strings;
		trySafe(() -> {
			symphonyService.sendMessage(symEvent, "The Task ID(s) seem to be invalid - "
					+ finalStrings.toString());
		}, false);
	}

	private boolean toCreateSubTask(UserInput userInput) {
		return !startTestingForJira(userInput).equals("");
	}

	private String startTestingForJira(UserInput userInput) {
		Optional<UserInputWord> jiraKey = userInput.getWords()
				.stream()
				.filter(userInputWord -> StringUtils.isNotBlank(userInputWord.getEntity()))
				.filter(userInputWord -> userInputWord.getEntity().equalsIgnoreCase("TASK"))
				.findFirst();
		if (jiraKey.isPresent()) {
			return jiraKey.get().getWord();
		} else {
			return "";
		}

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
