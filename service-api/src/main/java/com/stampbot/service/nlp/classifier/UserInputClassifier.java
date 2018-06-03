package com.stampbot.service.nlp.classifier;

import com.stampbot.service.symphony.ClassifierResponse;

public interface UserInputClassifier {

	ClassifierResponse classify(String input);
}
