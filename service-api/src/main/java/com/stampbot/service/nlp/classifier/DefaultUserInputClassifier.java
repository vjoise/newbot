package com.stampbot.service.nlp.classifier;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
class DefaultUserInputClassifier implements UserInputClassifier {

	@Override
	public ClassifierResponse classify(UserInput input) {
		ClassifierResponse response = new ClassifierResponse();
		List<String> collect = input.getWords().stream()
				.filter(word-> StringUtils.isNotBlank(word.getEntity()))
				.filter(word-> word.getEntity().contains("WORKFLOW"))
				.map(UserInputWord::getEntity).collect(Collectors.toList());
		log.info("Entities :: " + collect);
		if(!collect.isEmpty()){
			response.setResponseType(ClassifierResponseType.WORKFLOW);
			response.setMessage(collect.get(0));
		}
		return response;
	}
}
