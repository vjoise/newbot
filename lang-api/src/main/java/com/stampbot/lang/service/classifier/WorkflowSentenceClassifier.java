package com.stampbot.lang.service.classifier;

import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.domain.SentenceClassification;
import com.stampbot.lang.domain.SentenceClassificationTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkflowSentenceClassifier {

	@Autowired
	private BasicFilteredClassifier classifier;

	public SentenceClassification classify(InputSentence inputSentence) throws Exception {
		log.info("Input sentence is :: " + inputSentence.getSentence());
		String workflow = classifier.classify(inputSentence.getSentence());
		SentenceClassification classification = new SentenceClassification();
		classification.setSentenceClassificationTypeEnum(SentenceClassificationTypeEnum.NONE);
		if(!SentenceClassificationTypeEnum.NONE.name().equalsIgnoreCase(workflow)){
			classification.setSentenceClassificationTypeEnum(SentenceClassificationTypeEnum.WORKFLOW);
		}
		classification.setWorkflow(workflow);
		return classification;
	}

}
