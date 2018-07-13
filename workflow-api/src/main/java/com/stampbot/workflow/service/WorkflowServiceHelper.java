package com.stampbot.workflow.service;

import com.google.common.collect.Maps;
import com.stampbot.workflow.entity.UserWorkflowLogEntity;
import com.stampbot.workflow.entity.UserWorkflowMasterEntity;
import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import com.stampbot.workflow.model.WorkflowContext;
import com.stampbot.workflow.repository.UserWorkflowStore;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class WorkflowServiceHelper {

	@Autowired
	private UserWorkflowStore userWorkflowStore;

	public void stringToClass(String stringName, Class<?>[] actionHandlerClass) {
		try {
			actionHandlerClass[0] = Class.forName(stringName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void logWorkflowRecords(WorkflowContext context) {
		List<UserWorkflowLogEntity> logs = userWorkflowStore.findByConversationId(context.getConversationId());
		final Map<Long, UserWorkflowLogEntity> workflowLogMap = Maps.newHashMap();
		logs.forEach(log -> workflowLogMap.put(log.getId(), log));
		WorkflowQuestionEntity questionEntity = context.getQuestionEntity();
		if (questionEntity == null || questionEntity.getWorkflowEntity() == null) {
			return;
		}
		List<WorkflowQuestionEntity> questions = questionEntity.getWorkflowEntity().getQuestions();
		List<UserWorkflowLogEntity> logEntities = Lists.newArrayList();
		UserWorkflowMasterEntity userWorkflowMasterEntity = null;
		if (workflowLogMap.isEmpty()) {
			userWorkflowMasterEntity = new UserWorkflowMasterEntity();
		}
		UserWorkflowMasterEntity finalUserWorkflowMasterEntity = userWorkflowMasterEntity;
		questions.forEach(question -> {
			if (workflowLogMap.isEmpty()) {
				/*Log all the workflow items for this user here.*/
				UserWorkflowLogEntity entity = new UserWorkflowLogEntity();
				entity.setPassed(question.getId().equals(context.getQuestionEntity().getId()));
				entity.setConversationId(context.getConversationId());
				entity.setInputText(context.getInputSentence());
				entity.setStatus("ACTIVE");
				entity.setQuestionId(question.getId());
				entity.setUserId(context.getUserId());
				entity.setWorkflowId(question.getWorkflowEntity().getId());
				entity.setUserMentionIdsList(context.getUserIdMentionCSV());
				entity.setWorkflowMaster(finalUserWorkflowMasterEntity);
				logEntities.add(entity);
			} else {
				workflowLogMap.forEach((userId, workflowLog) -> {
					if (question.getId().equals(workflowLog.getQuestionId())) {
						UserWorkflowLogEntity log = userWorkflowStore.findById(workflowLog.getId());
						log.setPassed(true);
						log.setStatus("INACTIVE");
						logEntities.add(log);
					}
				});
			}
		});
		if (userWorkflowMasterEntity != null) {
			userWorkflowMasterEntity.setUserWorkflowLogEntities(logEntities);
			userWorkflowMasterEntity.setUserId(context.getUserId());
			userWorkflowMasterEntity.setConversationId(context.getConversationId());
			userWorkflowMasterEntity.setStatus("ACTIVE");
			userWorkflowStore.save(userWorkflowMasterEntity);
		} else {
			long inactiveCount = logEntities.stream().filter(entity -> entity.getStatus().equalsIgnoreCase("INACTIVE")).count();
			if (logEntities.size() == inactiveCount) {
				//mark the master one as inactive.
				UserWorkflowMasterEntity masterWorkflowEntity = userWorkflowStore.findMasterWorkflowEntity(context.getUserId(), context.getConversationId());
				masterWorkflowEntity.setStatus("INACTIVE");
				userWorkflowStore.save(masterWorkflowEntity);
			}
			userWorkflowStore.save(logEntities);
		}
	}

}
