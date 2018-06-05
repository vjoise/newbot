package com.stampbot.service.nlp.provider;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.domain.UserIntent;
import com.stampbot.service.nlp.MessageParser;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
class MessageParserImpl implements MessageParser {

	private static final Properties properties = new Properties();
	private static StanfordCoreNLP pipeline;

	@Autowired
	private ApplicationContext applicationContext;

	static {
		properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,regexner,depparse,natlog,openie,parse,sentiment");
		properties.put("regexner.mapping", "regexner.txt");
		pipeline = new StanfordCoreNLP(properties);
	}

	@Override
	public UserInput parseInputMessage(UserIntent intent) {
		UserInput userInput = intent.getCurrentInput();
		Annotation annotation = new Annotation(intent.getCurrentInput().getInputSentence());
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		sentences.forEach(sentence -> {
			String sentimentPolarity = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
			userInput.setNegativeSentiment("Negative".equalsIgnoreCase(sentimentPolarity.trim()));
			System.out.println(sentimentPolarity.trim());
			sentence.get(CoreAnnotations.TokensAnnotation.class).forEach(token -> {
				extractInputWord(userInput, token);
			});
			Collection<RelationTriple> relationTriples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
			System.out.println("relationTriples :: " + relationTriples);
			relationTriples.forEach(relation -> {
				System.out.println(relation.relationGloss());
				System.out.println(relation.subjectHead().value());
				System.out.println(relation.objectHead().value());
				userInput.setRelationTriple(relation);
			});
			SemanticGraph tree = sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class);
			System.out.println(tree.toString(SemanticGraph.OutputFormat.READABLE));
		});
		return userInput;
	}

	private void extractInputWord(UserInput userInput, CoreLabel token) {
		String word = token.get(CoreAnnotations.TextAnnotation.class);
		String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
		String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
		UserInputWord userInputWord = new UserInputWord(word, pos, ne);
		userInput.addWord(userInputWord);
		log.info(userInputWord.toString());
	}

	private void printSemanticGraph(CoreMap sentence) {
		Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		log.info("parse tree:\n" + tree);
		SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
		log.info("dependency graph:\n" + dependencies);
	}

	public static void main(String[] args) {
		//new MessageParserImpl().parseInputMessage("I will work on JIRA-123");
	}
}
