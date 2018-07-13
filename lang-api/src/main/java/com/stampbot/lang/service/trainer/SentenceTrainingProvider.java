package com.stampbot.lang.service.trainer;

import com.stampbot.lang.domain.FeatureSet;

public interface SentenceTrainingProvider {

	/*Given an input sentence, find out all the relevant features associated with it.*/
	FeatureSet prepare() throws Exception;

}
