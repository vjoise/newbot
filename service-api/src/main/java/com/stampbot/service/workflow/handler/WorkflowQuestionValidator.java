package com.stampbot.service.workflow.handler;

import com.stampbot.domain.UserInput;

public interface WorkflowQuestionValidator {

	boolean isValidInput(UserInput userInput);

}
