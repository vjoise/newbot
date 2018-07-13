package com.stampbot.workflow.service.provider;

import com.stampbot.workflow.entity.UserWorkflowLogEntity;
import com.stampbot.workflow.entity.WorkflowEntity;
import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import com.stampbot.workflow.model.WorkflowContext;
import com.stampbot.workflow.repository.UserWorkflowStore;
import com.stampbot.workflow.repository.WorkflowQuestionRepository;
import com.stampbot.workflow.repository.WorkflowRepository;
import com.stampbot.workflow.service.WorkflowServiceHelper;
import com.stampbot.workflow.service.type.WorkflowQuestionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;


@Component(WorkflowServiceProvider.BASIC)
@Slf4j
public class BasicWorkflowServiceProvider implements WorkflowServiceProvider {

	@Autowired
	private WorkflowRepository workflowRepository;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private WorkflowQuestionRepository questionRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private UserWorkflowStore userWorkflowStore;

	@Autowired
	private WorkflowServiceHelper workflowServiceHelper;

	public void save(WorkflowEntity entity) {
		workflowRepository.save(entity);
	}

	public void process(WorkflowContext workflowContext) {
		WorkflowQuestionEntity unansweredQuestion = null;
		UserWorkflowLogEntity nextUnansweredWorkflowLog = userWorkflowStore.findNextUnansweredQuestion(workflowContext.getConversationId());
		if (nextUnansweredWorkflowLog != null) {
			unansweredQuestion = questionRepository.findOne(nextUnansweredWorkflowLog.getQuestionId());
		} else {
			WorkflowEntity workflowEntity = workflowRepository.findByName(workflowContext.getWorkflowName());
			List<WorkflowQuestionEntity> byWorkflowName = questionRepository.findByWorkflowName(workflowEntity.getName());
			unansweredQuestion = byWorkflowName.get(0);
		}
		workflowContext.setQuestionEntity(unansweredQuestion);
		String actionHandler = unansweredQuestion.getActionHandler();
		log.info("Found the workflow associated to this chat :: " + unansweredQuestion.getWorkflowEntity().getName() + " and action handler :: " +
				actionHandler);
		workflowServiceHelper.logWorkflowRecords(workflowContext);
		if (StringUtils.isNotBlank(actionHandler)) {
			final Class<?>[] handlerClass = {null};
			workflowServiceHelper.stringToClass(actionHandler, handlerClass);
			WorkflowQuestionHandler quesionHandler = WorkflowQuestionHandler.class.cast(context.getBean(handlerClass[0]));
			quesionHandler.handle(workflowContext);
		} else {
			//respond to user with the current question configured.
			applicationContext.publishEvent(new SymphonyMessageEvent(this, workflowContext.getUserId(), unansweredQuestion.getQuestionText()));
		}
	}

}

