package com.stampbot.lang.service.trainer.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.domain.WordPOSMapping;
import org.nd4j.linalg.io.StringUtils;
import org.springframework.stereotype.Component;
import weka.core.Stopwords;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.SnowballStemmer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;

@Component
public class QuestionFeatureBuilder implements FeatureBuilder {

	List<String> verbCombos = Lists.newArrayList("VB",
			"VBD",
			"VBG",
			"VBN",
			"VBP",
			"VBZ",
			"WDT",
			"WP",
			"WP$",
			"WRB",
			"MD");

	List<String> questionTriples = Lists.newArrayList("CD-VB-VBN",
			"MD-PRP-VB",
			"MD-VB-CD",
			"NN-IN-DT",
			"PRP-VB-PRP",
			"PRP-WP-NNP",
			"VB-CD-VB",
			"VB-PRP-WP",
			"VBZ-DT-NN",
			"WP-VBZ-DT",
			"WP-VBZ-NNP",
			"WRB-MD-VB");

	List<String> statementTriples = Lists.newArrayList("DT-JJ-NN",
			"DT-NN-VBZ",
			"DT-NNP-NNP",
			"IN-DT-NN",
			"IN-NN-NNS",
			"MD-VB-VBN",
			"NNP-IN-NNP",
			"NNP-NNP-NNP",
			"NNP-VBZ-DT",
			"NNP-VBZ-NNP",
			"NNS-IN-DT",
			"VB-VBN-IN",
			"VBZ-DT-JJ");


	List<String> startTuples = Lists.newArrayList("NNS-DT", "WP-VBZ", "WRB-MD");

	List<String> endTuples = Lists.newArrayList("IN-NN", "VB-VBN", "VBZ-NNP");

	String stripSentence(String sentence) {
		sentence = sentence.replace(",", "");
		return sentence;
	}

	boolean existsPair(List<String> comboCheckList, InputSentence sentence) {
		String posTagsJoined = join(sentence.getWords().stream().map(WordPOSMapping::getPos).collect(Collectors.toList()), "-");
		List<String> allowedCombinations = Lists.newArrayList();
		comboCheckList.forEach(item -> {
			comboCheckList.forEach(inner -> {
				if (inner.equalsIgnoreCase(item)) {
					return;
				}
				allowedCombinations.add(join(new String[]{inner, item}, "-"));
			});
		});
		return allowedCombinations.contains(posTagsJoined);
	}

	private List<WordPOSMapping> getPOSTags(InputSentence sentence) {
		return sentence.getWords();
	}

	private int questionMarksCount(InputSentence sentence) {
		return StringUtils.countOccurrencesOf(sentence.getSentence(), "?");
	}

	private int countPOSType(InputSentence sentence, String posTag) {
		return StringUtils.countOccurrencesOf(sentence.getPosTags(), posTag);
	}

	private boolean verbExistsBeforeNoun(List<String> posTags) {
		//Strip the Verbs to all just "V"
		List<String> tempList = Lists.newArrayList();
		posTags.forEach(pos -> {
			pos = org.apache.commons.lang3.StringUtils.replacePattern(pos, "V.*", "V");
			pos = org.apache.commons.lang3.StringUtils.replacePattern(pos, "NN.*", "NN");
			tempList.add(pos);
		});
		return tempList.indexOf("V") < tempList.indexOf("N") ||
				tempList.indexOf("MD") < tempList.indexOf("N");
	}

	private boolean existsStemmedEndsWithNoun(InputSentence sentence) {
		String lastTuple = getFirstLastPOSTuples(sentence)[1];
		return Objects.requireNonNull(lastTuple).equalsIgnoreCase("NN-NN");
	}

	private String[] getFirstLastPOSTuples(InputSentence sentence) {
		int size = sentence.getPosTagList().size();
		int index = 0;
		if (size >= 3) {
			index = 2;
		} else {
			index = 1;
		}
		String firstTuple = join(sentence.getPosTagList().subList(0, index), "-");
		String lastTuple = join(sentence.getPosTagList().subList(size - 1, size), "-");
		return new String[]{firstTuple, lastTuple};
	}

	private List<Integer> existsTuple(List<String> tuples, String inputTuple) {
		List<Integer> exists = Lists.newArrayList();
		tuples.forEach(tuple -> {
			if (inputTuple.equalsIgnoreCase(tuple)) {
				exists.add(1);
			} else {
				exists.add(0);
			}
		});
		return exists;
	}

	private List<Integer> existsTriple(List<String> triples, List<String> tripleSet) {
		List<Integer> exists = Lists.newArrayList();
		tripleSet.forEach(triple -> {
			if (triples.contains(triple)) {
				exists.add(1);
			} else {
				exists.add(0);
			}
		});
		return exists;
	}

	private List<String> extractTriples(InputSentence sentence) {
		List<String> triples = Lists.newArrayList();
		int size = sentence.getPosTagList().size();
		if (size >= 3) {
			for (int i = 0; i < size - 2; i++) {
				triples.add(join(sentence.getPosTagList().subList(i, i + 3), "-"));
			}
		}
		return triples;
	}

	private List<String> lemmatize(InputSentence sentence) {
		return sentence.getWords().stream()
				.filter(wordPOSMapping -> Stopwords.isStopword(wordPOSMapping.getLemma()))
				.map(WordPOSMapping::getLemma)
				.collect(Collectors.toList());
	}

	private List<String> stemmatize(InputSentence sentence) {
		LovinsStemmer stemmer = new LovinsStemmer();
		return sentence.getWords().stream()
				.filter(wordPOSMapping -> Stopwords.isStopword(wordPOSMapping.getLemma()))
				.map(mapping -> stemmer.stem(mapping.getWord()))
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> build(InputSentence sentence) {
		Map<String, Object> featureMap = Maps.newHashMap();
		sentence.setSentence(stripSentence(sentence.getSentence()));
		featureMap.put("wordCount", sentence.getWords().size());
		featureMap.put("stemmedCount", stemmatize(sentence).size());
		featureMap.put("questionMarkCount", questionMarksCount(sentence));
		featureMap.put("verbCombo", existsPair(verbCombos, sentence) ? 1 : 0);
		featureMap.put("verbBeforeNoun", verbExistsBeforeNoun(sentence.getPosTagList()) ? 1 : 0);
		List<String> featuresList = Lists.newArrayList("VBG", "VBZ", "NNP", "NN", "NNS", "NNPS", "PRP", "CD");
		featuresList.forEach(feature -> {
			featureMap.put(feature, countPOSType(sentence, feature));
		});

		featureMap.put("stemmedEndWithNoun", existsStemmedEndsWithNoun(sentence) ? 1 : 0);
		String[] firstLastPOSTuples = getFirstLastPOSTuples(sentence);
		List<Integer> existsStartTuple = existsTuple(startTuples, firstLastPOSTuples[0]);
		List<Integer> existsEndTuple = existsTuple(endTuples, firstLastPOSTuples[1]);
		existsStartTuple.forEach(tuple -> {
			featureMap.put("startTuple" + tuple, tuple);
		});
		existsEndTuple.forEach(tuple -> {
			featureMap.put("endTuple" + tuple, tuple);
		});
		List<String> triples = extractTriples(sentence);
		List<Integer> existsQuestionTriples = existsTriple(triples, questionTriples);
		List<Integer> existsStatementTriples = existsTriple(triples, statementTriples);
		featureMap.put("qTripleScore", existsQuestionTriples.stream().filter(q -> q == 1).count());
		featureMap.put("sTripleScore", existsStatementTriples.stream().filter(s -> s == 1).count());

		return featureMap;
	}
}
