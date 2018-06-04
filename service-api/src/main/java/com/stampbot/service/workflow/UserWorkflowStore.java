package com.stampbot.service.workflow;

import com.stampbot.domain.UserInput;
import com.stampbot.entity.UserWorkflowLog;
import com.stampbot.repository.UserWorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserWorkflowStore {

	@Autowired
	private UserWorkflowRepository repository;

	public boolean isEmpty(UserInput userInput) {
		return repository.isEmpty(userInput.getUserId(), userInput.getConversationId());
	}

	public UserWorkflowLog findNextUnansweredQuestion(String conversationId){
		UserWorkflowLog log = repository.getUnansweredQuestion(conversationId);
		return log;
	}

	public List<UserWorkflowLog> findByConversationId(String conversationId) {
		List<UserWorkflowLog> logs = repository.findByConversationIdOrderById(conversationId);
		return logs;
	}

	public UserWorkflowLog findById(Long id) {
		return repository.findOne(id);
	}
}
