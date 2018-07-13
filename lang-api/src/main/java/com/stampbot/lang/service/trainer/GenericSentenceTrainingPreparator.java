package com.stampbot.lang.service.trainer;

import com.stampbot.lang.domain.FeatureSet;
import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.nlp.SentenceParser;
import com.stampbot.lang.service.trainer.feature.FeatureBuilder;
import com.stampbot.lang.service.trainer.feature.QuestionFeatureBuilder;
import edu.stanford.nlp.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
class GenericSentenceTrainingPreparator implements SentenceTrainingProvider {

	@Autowired
	private FeatureBuilder featureBuilder;

	@Autowired
	private SentenceParser sentenceParser;

	public FeatureSet prepare() throws Exception {
		FeatureSet featureSet = new FeatureSet();
		Scanner scanner = new Scanner(new ClassPathResource("generic_sentences.csv").getInputStream());
		String path = "C:\\Users\\deprasa\\Desktop\\hackathon\\new-bot\\lang-api\\src\\main\\resources\\generic-sentence-training.arrf";
		StringBuilder builder = new StringBuilder();
		byte[] headerBytes = org.apache.commons.io.IOUtils.toByteArray(new ClassPathResource("generic_sentences_header.txt").getInputStream());
		String features = "";
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split(",");
			String className = line[0];
			String sentence = line[1];
			InputSentence inputSentence = sentenceParser.parseInputMessage(sentence);
			Map<String, Object> featureMap = featureBuilder.build(inputSentence);
			if (StringUtils.isBlank(features)) {
				features = StringUtils.join(featureMap.keySet(), ",");
				builder.append(new String(headerBytes)).append("\r\n");
			}
			builder.append(className).append(",");
			featureMap.forEach((key, value) -> {
				builder.append(value);
				builder.append(",");
			});
			builder.append("\r\n");
		}
		IOUtils.writeStringToFile(builder.toString(), path, "UTF-8");
		return featureSet;
	}

	public static void main(String[] args) throws Exception {
		GenericSentenceTrainingPreparator genericSentenceTrainingProvider = new GenericSentenceTrainingPreparator();
		genericSentenceTrainingProvider.featureBuilder = new QuestionFeatureBuilder();
		genericSentenceTrainingProvider.sentenceParser = new SentenceParser();
		genericSentenceTrainingProvider.prepare();

	}
}

