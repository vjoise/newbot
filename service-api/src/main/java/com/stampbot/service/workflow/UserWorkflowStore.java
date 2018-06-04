package com.stampbot.service.workflow;

import com.stampbot.domain.UserInput;
import com.stampbot.entity.UserWorkflowLogEntity;
import com.stampbot.entity.UserWorkflowMasterEntity;
import com.stampbot.repository.UserWorkflowRepository;
import com.stampbot.repository.UserWorkflowMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserWorkflowStore {

	@Autowired
	private UserWorkflowRepository repository;

	@Autowired
	private UserWorkflowMasterRepository masterRepository;

	public boolean isEmpty(UserInput userInput) {
		return masterRepository.isEmpty(userInput.getUserId(), userInput.getConversationId());
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
		return repository.findQuestionWithNextQuestionId(aLong, id);
	}
}
