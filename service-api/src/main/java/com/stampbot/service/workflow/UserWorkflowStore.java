package com.stampbot.service.workflow;

import com.stampbot.domain.UserInput;
import com.stampbot.repository.UserWorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserWorkflowStore {

	@Autowired
	private UserWorkflowRepository repository;

	public boolean isEmpty(UserInput userInput) {
		return repository.isEmpty(userInput.getUserId(), userInput.getConversationId());
	}

}
