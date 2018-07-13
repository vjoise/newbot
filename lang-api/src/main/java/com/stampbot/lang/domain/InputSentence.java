package com.stampbot.lang.domain;

import com.google.common.collect.Lists;
import edu.stanford.nlp.ie.util.RelationTriple;
import lombok.Data;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InputSentence {

	@NotNull
	private String sentence;

	private String userId;
	private RelationTriple relationTriple;

	public InputSentence(String messageText) {
		this.sentence = messageText;
	}

	private SentenceRelation relation;

	private String subject;

	private String object;

	private List<WordPOSMapping> words = Lists.newArrayList();

	public String getPosTags(){
		return words.stream().map(WordPOSMapping::getPos).collect(Collectors.joining("-"));
	}

	public List<String> getPosTagList(){
		return words.stream().map(WordPOSMapping::getPos).collect(Collectors.toList());
	}

	public String getStemmedSentence(){
		Stemmer stemmer = new SnowballStemmer();
		return stemmer.stem(sentence);
	}

	public void addWord(WordPOSMapping userInputWord) {
		this.words.add(userInputWord);
	}
}
