package com.stampbot.service.workflow;

import com.google.common.collect.Maps;
import com.stampbot.domain.UserInput;
import com.stampbot.entity.ActionItemEntity;
import com.stampbot.entity.UserWorkflowLogEntity;
import com.stampbot.entity.WorkflowEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.repository.WorkflowRepository;
import com.stampbot.service.action.ActionItemService;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.workflow.handler.WorkflowQuesionHandler;
import com.stampbot.service.workflow.handler.WorkflowQuestionValidator;
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
		final Class<?>[] handlerClass = {null};
		WorkflowQuestionEntity unansweredQuestion = null;
		UserWorkflowLogEntity nextUnansweredWorkflowLog = userWorkflowStore.findNextUnansweredQuestion(userInput.getConversationId());
		if(nextUnansweredWorkflowLog != null){
			unansweredQuestion = questionRepository.findOne(nextUnansweredWorkflowLog.getQuestionId());
		}else{
			WorkflowEntity workflowEntity = workflowRepository.findByName(userInput.getDetectedWorkflow());
			List<WorkflowQuestionEntity> byWorkflowName = questionRepository.findByWorkflowName(workflowEntity.getName());
			unansweredQuestion = byWorkflowName.get(0);
		}
		userInput.setQuestionEntity(unansweredQuestion);
		log.info("Found the workflow associated to this chat :: " + unansweredQuestion.getWorkflowEntity().getName() + " and action handler :: " +
				unansweredQuestion.getActionHandler());
		stringToClass(unansweredQuestion.getActionHandler(), handlerClass);
		logWorkflowRecords(symEvent, userInput);
		if (handlerClass[0] != null) {
			WorkflowQuesionHandler quesionHandler = WorkflowQuesionHandler.class.cast(context.getBean(handlerClass[0]));
			quesionHandler.handle(workflowContext);
		} else {
			//respond to user with the current question configured.
			WorkflowQuestionEntity finalUnansweredQuestion = unansweredQuestion;
			trySafe(() -> symphonyService.sendMessage(symEvent, finalUnansweredQuestion.getQuestionText()), false);
		}
	}

	public void stringToClass(String stringName, Class<?>[] actionHandlerClass) {
		trySafe(() -> {
			actionHandlerClass[0] = Class.forName(stringName);
		}, false);
	}

	private void logWorkflowRecords(SymEvent symEvent, UserInput userInput) {
		List<UserWorkflowLogEntity> logs = userWorkflowStore.findByConversationId(symEvent.getPayload().getMessageSent().getStreamId());
		final Map<Long, UserWorkflowLogEntity> workflowLogMap = Maps.newHashMap();
		logs.forEach(log -> workflowLogMap.put(log.getId(), log));
		WorkflowQuestionEntity questionEntity = userInput.getQuestionEntity();
		if (questionEntity == null || questionEntity.getWorkflowEntity() == null) {
			return;
		}
		List<WorkflowQuestionEntity> questions = questionEntity.getWorkflowEntity().getQuestions();
		List<UserWorkflowLogEntity> logEntities = Lists.newArrayList();
		questions.forEach(question -> {
			if (workflowLogMap.isEmpty()) {
				/*Log all the workflow items for this user here.*/
				UserWorkflowLogEntity entity = new UserWorkflowLogEntity();
				entity.setPassed(question.getId().equals(userInput.getQuestionEntity().getId()));
				entity.setConversationId(userInput.getConversationId());
				entity.setInputText(userInput.getInputSentence());
				entity.setQuestionId(question.getId());
				entity.setUserId(userInput.getUserId());
				logEntities.add(entity);
			} else {
				workflowLogMap.forEach((userId, workflowLog) -> {
					if (question.getId().equals(workflowLog.getQuestionId())) {
						UserWorkflowLogEntity log = userWorkflowStore.findById(workflowLog.getId());
						log.setPassed(true);
						logEntities.add(log);
					}
				});
			}
		});
		userWorkflowStore.save(logEntities);

	}

}
