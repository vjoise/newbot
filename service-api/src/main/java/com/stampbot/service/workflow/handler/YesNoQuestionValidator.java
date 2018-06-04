package com.stampbot.service.workflow.handler;

import com.stampbot.domain.UserInput;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class YesNoQuestionValidator implements WorkflowQuestionValidator {

	private List<String> validUserInputs = Lists.newArrayList("yes", "no");

	@Override
	public boolean isValidInput(UserInput userInput) {
		return !CollectionUtils.isEmpty(userInput.getWords()) && userInput.getWords().size() == 1
				&& validUserInputs.contains(userInput.getWords().iterator().next().getWord().toLowerCase());
	}
}
