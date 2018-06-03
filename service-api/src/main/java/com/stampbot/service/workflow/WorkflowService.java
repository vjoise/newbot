package com.stampbot.service.workflow;

import com.stampbot.domain.UserInput;
import com.stampbot.entity.ActionItemEntity;
import com.stampbot.entity.WorkflowEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.action.ActionItemService;
import com.stampbot.service.workflow.handler.WorkflowQuesionHandler;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.stampbot.common.Utils.trySafe;

@Component
@Slf4j
public class WorkflowService {

	@Autowired
	private WorkflowRepository workflowRepository;

	@Autowired
	private ActionItemService actionItemService;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private WorkflowQuestionRepository questionRepository;

	public void save(WorkflowEntity entity) {
		workflowRepository.save(entity);
	}

	public void process(Map<String, Object> workflowContext) {
		UserInput userInput = UserInput.class.cast(workflowContext.get("userInput"));
		List<ActionItemEntity> actionItemEntities = Lists.newArrayList();
		log.info("Persisting Workflow Action Items for user...");
		actionItemService.createActionItems(actionItemEntities);
		WorkflowQuestionEntity byWorkflowName = questionRepository.findByWorkflowName(userInput.getDetectedWorkflow());
		log.info("Found the workflow associated to this chat :: " + byWorkflowName + " and action handler :: " +
				byWorkflowName.getActionHandler());
		final Class<?>[] actionHandlerClass = {null};
		trySafe(()-> {
			actionHandlerClass[0] = Class.forName(byWorkflowName.getActionHandler());
		}, true);
		if (actionHandlerClass[0] != null) {
			WorkflowQuesionHandler quesionHandler = WorkflowQuesionHandler.class.cast(context.getBean(actionHandlerClass[0]));
			quesionHandler.handle(workflowContext);
		}
	}
}
