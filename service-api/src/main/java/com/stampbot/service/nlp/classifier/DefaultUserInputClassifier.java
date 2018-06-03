package com.stampbot.service.nlp.classifier;

import com.stampbot.service.symphony.ClassifierResponse;
import com.stampbot.service.symphony.ClassifierResponseType;
import org.springframework.stereotype.Component;

@Component
class DefaultUserInputClassifier implements UserInputClassifier{

	@Override
	public ClassifierResponse classify(String input) {
		ClassifierResponse response = new ClassifierResponse();
		response.setResponseType(ClassifierResponseType.NO_MATCH_FOUND);
		response.setMessage("NEGATIVE");
		return response;
	}
}
