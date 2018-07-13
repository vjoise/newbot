package com.stampbot.lang.domain;

import lombok.Data;

import java.util.List;

@Data
public class SentenceClassification {

	private SentenceClassificationTypeEnum sentenceClassificationTypeEnum;

	private SentenceTypeEnum typeEnum = SentenceTypeEnum.NONE;

	private String workflow;

}
