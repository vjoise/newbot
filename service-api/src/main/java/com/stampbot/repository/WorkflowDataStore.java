package com.stampbot.repository;

import com.stampbot.entity.WorkflowEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.service.workflow.WorkflowService;
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
		List<WorkflowQuestionEntity> questions = Lists.newArrayList();
		WorkflowQuestionEntity entity = WorkflowQuestionEntity.builder()
				.questionKey("JIRA_QUESTION")
				.questionText("You have provided the following, please confirm with Yes or No")
				.build();
		questions.add(entity);
		workflowEntity.setQuestions(questions);
		workflowService.save(workflowEntity);
	}

	public long count() {
		return workflowRepository.count();
	}

}
