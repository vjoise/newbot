package com.stampbot.service.nlp.classifier;

import lombok.Data;

@Data
public class ClassifierResponse {

	private ClassifierResponseType responseType = ClassifierResponseType.WORKFLOW;

	private String message;
}
