package com.stampbot.lang.service.trainer.feature;

import com.stampbot.lang.domain.InputSentence;

import java.util.Map;

public interface FeatureBuilder {

	Map<String, Object> build(InputSentence sentence);
}
