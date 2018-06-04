package com.stampbot.service.workflow;

import com.google.common.collect.Maps;
import com.stampbot.domain.UserInput;
import com.stampbot.entity.ActionItemEntity;
import com.stampbot.entity.UserWorkflowLog;
import com.stampbot.entity.WorkflowEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.action.ActionItemService;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.workflow.handler.WorkflowQuesionHandler;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.events.SymEvent;

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

	@Autowired
	private SymphonyService symphonyService;

	@Autowired
	private UserWorkflowStore userWorkflowStore;

	public void save(WorkflowEntity entity) {
		workflowRepository.save(entity);
	}

	public void process(Map<String, Object> workflowContext) {
		UserInput userInput = UserInput.class.cast(workflowContext.get("userInput"));
		SymEvent symEvent = SymEvent.class.cast(workflowContext.get("symEvent"));
		List<ActionItemEntity> actionItemEntities = Lists.newArrayList();
		log.info("Persisting Workflow Action Items for user...");
		actionItemService.createActionItems(actionItemEntities);
		boolean freshUser = userWorkflowStore.isEmpty(userInput);
		if (freshUser) {
			logWorkflowRecords(symEvent, userInput);
		}
		WorkflowQuestionEntity byWorkflowName = questionRepository.findByWorkflowName(userInput.getDetectedWorkflow());
		log.info("Found the workflow associated to this chat :: " + byWorkflowName + " and action handler :: " +
				byWorkflowName.getActionHandler());
		final Class<?>[] actionHandlerClass = {null};
		trySafe(() -> {
			actionHandlerClass[0] = Class.forName(byWorkflowName.getActionHandler());
		}, false);
		if (actionHandlerClass[0] != null) {
			WorkflowQuesionHandler quesionHandler = WorkflowQuesionHandler.class.cast(context.getBean(actionHandlerClass[0]));
			quesionHandler.handle(workflowContext);
		} else {
			//respond to user with the current question configured.
			trySafe(() -> symphonyService.sendMessage(symEvent, byWorkflowName.getQuestionText()), false);
		}
	}

	private void logWorkflowRecords(SymEvent symEvent, UserInput userInput) {
		List<UserWorkflowLog> logs = userWorkflowStore.findByConversationId(symEvent.getPayload().getMessageSent().getStreamId());
		final Map<Long, UserWorkflowLog> workflowLogMap = Maps.newHashMap();
		logs.forEach(log -> workflowLogMap.put(log.getId(), log));
		WorkflowQuestionEntity questionEntity = userInput.getQuestionEntity();
		if (questionEntity == null || questionEntity.getWorkflowEntity() == null) {
			return;
		}
		List<WorkflowQuestionEntity> questions = questionEntity.getWorkflowEntity().getQuestions();
		questions.forEach(question -> {
			workflowLogMap.forEach((userId, workflowLog) -> {
				if (question.getId().equals(workflowLog.getQuestionId())) {
					UserWorkflowLog log = userWorkflowStore.findById(workflowLog.getId());
					log.setPassed(true);
				}
			});
		});
	}

}
