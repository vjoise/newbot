package com.stampbot.service.nlp.classifier;

import com.stampbot.domain.UserInput;

public interface UserInputClassifier {

	ClassifierResponse classify(UserInput input);
}
