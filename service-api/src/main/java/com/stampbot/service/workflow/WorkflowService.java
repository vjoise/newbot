package com.stampbot.service.workflow;

import com.stampbot.entity.ActionItemEntity;
import com.stampbot.entity.WorkflowEntity;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.action.ActionItemService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowService {

	@Autowired
	private WorkflowRepository workflowRepository;

	@Autowired
	private ActionItemService actionItemService;


	public void save(WorkflowEntity entity) {
		workflowRepository.save(entity);
	}

	public void process(String message) {
		List<ActionItemEntity> actionItemEntities = Lists.newArrayList();
		actionItemService.createActionItems(actionItemEntities);
	}
}
