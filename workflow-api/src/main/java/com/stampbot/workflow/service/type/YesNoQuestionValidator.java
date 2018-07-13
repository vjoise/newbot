package com.stampbot.workflow.service.type;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YesNoQuestionValidator implements WorkflowQuestionValidator {

	private List<String> validUserInputs = Lists.newArrayList("yes", "no");

	@Override
	public boolean isValidInput(String input) {
		return StringUtils.isNotBlank(input) && input.split(" ").length == 1
				&& validUserInputs.contains(input.toLowerCase());
	}
}
