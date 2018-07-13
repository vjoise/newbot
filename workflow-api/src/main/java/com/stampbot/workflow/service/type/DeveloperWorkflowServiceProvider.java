package com.stampbot.workflow.service.type;

import com.stampbot.workflow.entity.UserWorkflowLogEntity;
import com.stampbot.workflow.model.Sprint;
import com.stampbot.workflow.model.WorkflowContext;
import com.stampbot.workflow.model.issueModel.IssueResponse;
import com.stampbot.workflow.repository.UserWorkflowStore;
import com.stampbot.workflow.repository.WorkflowQuestionRepository;
import com.stampbot.workflow.repository.WorkflowRepository;
import com.stampbot.workflow.service.provider.SymphonyMessageEvent;
import com.stampbot.workflow.service.task.JiraTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.stampbot.common.Utils.trySafe;

//@Component("dev_workflow")
@Slf4j
class DeveloperWorkflowServiceProvider implements WorkflowQuestionHandler {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	private String processInstanceId;

	@Autowired
	private WorkflowRepository workflowRepository;

	@Autowired
	private WorkflowQuestionRepository questionRepository;

	@Autowired
	private JiraTaskService jiraTaskService;

	@Autowired
	private UserWorkflowStore userWorkflowStore;

	@Autowired
	private ApplicationContext applicationContext;

	@EventListener
	public void notify(final PostDeployEvent unused) {
		log.info("Task initialized now :: DevJiraWorkflow");
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void execute(DelegateExecution delegateExecution) throws Exception {
		List<String> assignedToList = List.class.cast(delegateExecution.getVariable("assignedTo"));
		String jiraId = String.class.cast(delegateExecution.getVariable("jiraId"));
		String processInstanceId = delegateExecution.getProcessInstanceId();
		log.info("Process Instance ID for DevJiraWorkflow :: " + processInstanceId);
		delegateExecution.setVariable("botResponse", "Thank you for using KAKI, have a Productive Day!");
		log.info("Jira ID : {}", jiraId);
		log.info("Assigned To : {}", assignedToList);
	}

	public void handle(WorkflowContext context) {
	   /*mockPreviousInput(context);
	   printIssuesForSprint(context);
       printIssuesForSprint(context);*/
	   applicationContext.publishEvent(new SymphonyMessageEvent(this, context.getUserId(), "Raising a JIRA Sub Task for user"));
	}

	private void mockPreviousInput(WorkflowContext context) {
		UserWorkflowLogEntity previousWorkflowLogEntity = userWorkflowStore.findQuestionWithNextQuestionId(
				context.getQuestionEntity().getId(),
				context.getQuestionEntity().getWorkflowEntity().getId());
		List<String> previousWords = Arrays.asList(previousWorkflowLogEntity.getInputText().split(" "));
	}

	private void printIssuesForSprint(WorkflowContext context) {
		List<IssueResponse> issuesForSprint = getIssuesForSprint(context);
		if (issuesForSprint != null && issuesForSprint.size() > 0) {
			/*Send a Symphony message event.*/
			trySafe(() -> {
				String message = "Found " + issuesForSprint.size() + " issues in this sprint :";
				applicationContext.publishEvent(new SymphonyMessageEvent(this, context.getUserId(), message));
				printIssues(context, issuesForSprint);
			}, false);
		}
	}

	private void printIssues(WorkflowContext context, List<IssueResponse> issuesForSprint) {
		issuesForSprint.forEach(issue -> trySafe(() -> {
			String message = issue.getKey() + " : "
					+ issue.getFields().getStatus() + " : "
					+ issue.getFields().getSummary();
			applicationContext.publishEvent(
					new SymphonyMessageEvent(this,
							context.getUserId(), message));
		}, false));
	}

	private List<IssueResponse> getIssuesForSprint(WorkflowContext context) {
		String sprintName = toGetIssues(context.getInputSentence());
		if (StringUtils.isNotBlank(sprintName)) {
			trySafe(() -> {
				applicationContext.publishEvent(
						new SymphonyMessageEvent(this,
								context.getUserId(),
								"Found sprint :: " + sprintName));
			}, false);
			List<IssueResponse> sprints = jiraTaskService.getIssuesForSprintName(sprintName);
			if (sprints.size() > 0) {
				return sprints;
			}
		}
		return null;
	}

	private String toGetIssues(String sentence) {
		return foundSprintName(sentence);
	}

	private String foundSprintName(String inputSentence) {
		if (contains(inputSentence, "release") || contains(inputSentence, "version")) {
			List<Sprint> allSprints = jiraTaskService.getSprints(jiraTaskService.getBoardId()).getValues();
			if (allSprints.size() > 0) {
				List<String> allSprintNames = allSprints.stream()
						.map(Sprint::getName)
						.collect(Collectors.toList());
				List<String> foundQuotedString = findQuotedString(inputSentence);
				if (foundQuotedString.size() > 0) {
					List<String> sprintNames = foundQuotedString.stream()
							.map(string -> string.replaceAll("\"", ""))
							.filter(sprintName -> jiraTaskService.getMatchingNameSprintId(sprintName) >= 0)
							.collect(Collectors.toList());
					if (sprintNames.size() > 0) {
						return sprintNames.get(0);
					}
				}
			}
		}
		return "";
	}

	private List<String> findQuotedString(String inputSentence) {
		Pattern pattern = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1");
		Matcher matcher = pattern.matcher(inputSentence);
		boolean matches = matcher.matches();
		List<String> foundSprintNames = new ArrayList<>();
		while (matcher.find()) {
			foundSprintNames.add(matcher.group(1));
		}
		return foundSprintNames;
	}

	private boolean contains(String sentence, String word) {
		return sentence.contains(word);
	}

}
