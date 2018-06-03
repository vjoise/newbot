package com.stampbot.repository;

import com.stampbot.entity.WorkflowEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.service.workflow.WorkflowService;
import com.stampbot.service.workflow.handler.JiraQuestionWorkflowHandler;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowDataStore {

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private WorkflowRepository workflowRepository;

	public void build() {
		WorkflowEntity workflowEntity = new WorkflowEntity();
		workflowEntity.setName("JIRA_WORKFLOW");
		WorkflowQuestionEntity jiraQuestion1 = new WorkflowQuestionEntity();
		jiraQuestion1.setQuestionKey("JIRA_QUESTION");
		jiraQuestion1.setQuestionText("You have provided the following, please confirm with Yes or No");
		jiraQuestion1.setWorkflowEntity(workflowEntity);
		WorkflowQuestionEntity jiraQuestion2 = new WorkflowQuestionEntity();
		jiraQuestion2.setActionHandler(JiraQuestionWorkflowHandler.class.getName());
		jiraQuestion1.setNextQuestion(jiraQuestion2);
		workflowEntity.setQuestions(Lists.newArrayList(jiraQuestion1, jiraQuestion2));

		workflowService.save(workflowEntity);
	}

	public long count() {
		return workflowRepository.count();
	}

}
