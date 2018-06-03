package com.stampbot.service.symphony;

import lombok.Data;

@Data
public class ClassifierResponse {

	private ClassifierResponseType responseType = ClassifierResponseType.NO_MATCH_FOUND;

	private String message;
}
