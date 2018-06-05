package com.stampbot.repository;

import com.stampbot.entity.WorkflowEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.service.workflow.WorkflowService;
import com.stampbot.service.workflow.handler.DevQuestionWorkflowHandler;
import com.stampbot.service.workflow.handler.JiraQuestionWorkflowHandler;
import com.stampbot.service.workflow.handler.YesNoQuestionValidator;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkflowDataStore {

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private WorkflowRepository workflowRepository;

	public void build() {
		WorkflowEntity workflowEntity = new WorkflowEntity();
		workflowEntity.setName("DEV_WORKFLOW");
		WorkflowQuestionEntity jiraQuestion1 = new WorkflowQuestionEntity();
		jiraQuestion1.setQuestionKey("JIRA_QUESTION");
		jiraQuestion1.setQuestionText("You have provided the following, please confirm with Yes or No");
		jiraQuestion1.setWorkflowEntity(workflowEntity);
		WorkflowQuestionEntity jiraQuestion2 = new WorkflowQuestionEntity();
		jiraQuestion2.setInputValidator(YesNoQuestionValidator.class.getName());
		jiraQuestion2.setActionHandler(JiraQuestionWorkflowHandler.class.getName());
		jiraQuestion1.setNextQuestion(jiraQuestion2);
		jiraQuestion2.setWorkflowEntity(workflowEntity);
		workflowEntity.setQuestions(Lists.newArrayList(jiraQuestion1, jiraQuestion2));

		workflowService.save(workflowEntity);
	}

	public void buildForDev() {
		WorkflowEntity workflowEntity = new WorkflowEntity();
		workflowEntity.setName("DEV_WORKFLOW");
		WorkflowQuestionEntity devQuestion1 = new WorkflowQuestionEntity();
		devQuestion1.setQuestionKey("DEV_QUESTION");
		devQuestion1.setQuestionText("You are a DEVELOPER and have requested for an action, please confirm with Yes or No");
		devQuestion1.setWorkflowEntity(workflowEntity);
		WorkflowQuestionEntity devQuestion2 = new WorkflowQuestionEntity();
		devQuestion2.setActionHandler(DevQuestionWorkflowHandler.class.getName());
		devQuestion1.setNextQuestion(devQuestion2);
		workflowEntity.setQuestions(Lists.newArrayList(devQuestion1, devQuestion2));

		workflowService.save(workflowEntity);
	}

	public long count() {
		return workflowRepository.count();
	}

}
