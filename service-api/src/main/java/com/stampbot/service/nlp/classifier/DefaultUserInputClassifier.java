package com.stampbot.service.nlp.classifier;

import org.springframework.stereotype.Component;

@Component
class DefaultUserInputClassifier implements UserInputClassifier{

	@Override
	public ClassifierResponse classify(String input) {
		ClassifierResponse response = new ClassifierResponse();
		response.setResponseType(ClassifierResponseType.WORKFLOW);
		response.setMessage("JIRA_WORKFLOW");
		return response;
	}
}
