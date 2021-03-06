package com.stampbot.workflow.repository;

import com.stampbot.workflow.entity.UserWorkflowLogEntity;
import com.stampbot.workflow.entity.UserWorkflowMasterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class UserWorkflowStore {

	@Autowired
	private UserWorkflowRepository repository;

	@Autowired
	private UserWorkflowMasterRepository masterRepository;

	public boolean isEmpty(Long userId, String conversationId) {
		return masterRepository.isEmpty(userId, conversationId);
	}

	public UserWorkflowLogEntity findNextUnansweredQuestion(String conversationId){
		return repository.getUnansweredQuestion(conversationId);
	}

	public UserWorkflowLogEntity findNextUnansweredQuestionByWorkflow(String workflowId){
		return repository.getUnansweredQuestionGivenWorkflow(workflowId);
	}

	public List<UserWorkflowLogEntity> findByConversationId(String conversationId) {
		return repository.findByConversationIdOrderById(conversationId);
	}

	public UserWorkflowLogEntity findById(Long id) {
		return repository.findOne(id);
	}

	public void save(List<UserWorkflowLogEntity> logEntities) {
		repository.save(logEntities);
	}

	public void save(UserWorkflowMasterEntity entity){
		masterRepository.save(entity);
	}

	public UserWorkflowMasterEntity findMasterWorkflowEntity(Long userId, String conversationId){
		return masterRepository.findByUserIdAndConversationId(userId, conversationId);
	}

	public UserWorkflowLogEntity findQuestionWithNextQuestionId(Long aLong, Long id) {
		UserWorkflowLogEntity entity = new UserWorkflowLogEntity();
		List<UserWorkflowLogEntity> questionWithNextQuestionId = repository.findQuestionWithNextQuestionId(aLong, id);
		return CollectionUtils.isEmpty(questionWithNextQuestionId) ? null : questionWithNextQuestionId.get(0);
	}

	public UserWorkflowMasterEntity findMasterWorkflowEntityById(Long masterWorkflowId) {
		return masterRepository.findOne(masterWorkflowId);
	}
}
