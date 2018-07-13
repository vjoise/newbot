package com.stampbot.lang.domain;

import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

@Data
public class FeatureSet {

	private SentimentPolarityEnum polarity = SentimentPolarityEnum.NEUTRAL;

	private Set<LangFeature> featureList = Sets.newHashSet();

	private SentenceTypeEnum sentenceTypeEnum = SentenceTypeEnum.NONE;

	private InputSentence inputSentence;

	public void add(LangFeature feature) {
		this.featureList.add(feature);
	}
}
