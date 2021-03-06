package com.stampbot.workflow.repository;

import com.google.common.collect.Lists;
import com.stampbot.workflow.entity.WorkflowEntity;
import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import com.stampbot.workflow.service.WorkflowService;
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
		jiraQuestion1.setQuestionText("Please confirm with Yes to submit a task or No to decline!");
		jiraQuestion1.setWorkflowEntity(workflowEntity);
		WorkflowQuestionEntity jiraQuestion2 = new WorkflowQuestionEntity();
		//jiraQuestion2.setInputValidator(YesNoQuestionValidator.class.getName());
		//jiraQuestion2.setActionHandler(JiraQuestionWorkflowHandler.class.getName());
		jiraQuestion1.setNextQuestion(jiraQuestion2);
		jiraQuestion2.setWorkflowEntity(workflowEntity);
		workflowEntity.setQuestions(Lists.newArrayList(jiraQuestion1, jiraQuestion2));
		workflowRepository.save(workflowEntity);
	}

	public void buildForDev() {
		WorkflowEntity workflowEntity = new WorkflowEntity();
		workflowEntity.setName("user_workflow");
		WorkflowQuestionEntity devQuestion1 = new WorkflowQuestionEntity();
		devQuestion1.setQuestionKey("JIRA_QUESTION");
		devQuestion1.setQuestionText("You are a User and have requested for an action, please confirm with Yes or No");
		devQuestion1.setWorkflowEntity(workflowEntity);
		WorkflowQuestionEntity devQuestion2 = new WorkflowQuestionEntity();
		devQuestion2.setActionHandler("com.stampbot.workflow.service.type.UserWorkflowServiceProvider");
		devQuestion1.setNextQuestion(devQuestion2);
		devQuestion2.setWorkflowEntity(workflowEntity);
		workflowEntity.setQuestions(Lists.newArrayList(devQuestion1, devQuestion2));
		workflowRepository.save(workflowEntity);
	}

	public long count() {
		return workflowRepository.count();
	}

}
