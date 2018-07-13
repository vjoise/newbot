package com.stampbot.service.nlp.provider;

import com.google.common.collect.Lists;
import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.domain.UserIntent;
import com.stampbot.lang.domain.SentenceRelation;
import com.stampbot.service.nlp.MessageParser;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
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
import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.text.stopwords.StopWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import weka.core.Stopwords;
import weka.core.tokenizers.NGramTokenizer;

import java.util.*;
import java.util.stream.Collectors;

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
			IndexedWord firstRoot = tree.getFirstRoot();
			Set<IndexedWord> children = tree.getChildren(firstRoot);
			String rootPOS = firstRoot.get(CoreAnnotations.PartOfSpeechAnnotation.class);
			/*If first root is neither LS nor VERB, then check with all NGRAMs from 3 to 6*/
			List<String> validQuestionPOS = Lists.newArrayList("LS", "VB", "VBG");
			if (validQuestionPOS.contains(rootPOS)) {
				final SentenceRelation relation = new SentenceRelation();
				children.forEach(child -> {
					String pos = child.get(CoreAnnotations.PartOfSpeechAnnotation.class);
					String text = child.get(CoreAnnotations.TextAnnotation.class);
					if (pos.equalsIgnoreCase("WP")) {
						relation.setSubject(text);
					} else if (pos.equalsIgnoreCase("VB") || pos.equalsIgnoreCase("VBG")) {
						relation.setRelation(text);
					} else if (pos.equalsIgnoreCase("NN")) {
						relation.setObject(text);
					}
				});
				if (StringUtils.isBlank(relation.getRelation())) {
					if (rootPOS.matches("VB.*")) {
						relation.setRelation(firstRoot.get(CoreAnnotations.LemmaAnnotation.class));
					}
				}
				userInput.setSentenceRelation(relation);
				System.out.println(relation);
			} else {
				for (int ngramSize = 4; ngramSize <= 6; ngramSize++) {
					List<String> strings = buildNGrams(intent.getCurrentInput().getInputSentence(), ngramSize);
					strings = strings.stream().filter(Stopwords::isStopword).collect(Collectors.toList());
					System.out.println(strings);
					Annotation nGramAnnotation = new Annotation(intent.getCurrentInput().getInputSentence());
					pipeline.annotate(nGramAnnotation);
				}
			}
			System.out.println(tree.toString(SemanticGraph.OutputFormat.LIST));
		});
		return userInput;
	}

	private List<String> buildNGrams(String sentence, int ngramSize) {
		NGramTokenizer nGramTokenizer = new NGramTokenizer();
		nGramTokenizer.setNGramMinSize(3);
		nGramTokenizer.setNGramMaxSize(3);
		nGramTokenizer.tokenize(sentence);
		List<String> ngrams = Lists.newArrayList();
		while (nGramTokenizer.hasMoreElements()) {
			ngrams.add(nGramTokenizer.nextElement());
		}
		return ngrams;
	}

	private void extractInputWord(UserInput userInput, CoreLabel token) {
		String word = token.get(CoreAnnotations.TextAnnotation.class);
		String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
		String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
		UserInputWord userInputWord = new UserInputWord(word, pos, ne);
		userInputWord.setLemma(token.get(CoreAnnotations.LemmaAnnotation.class));
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
		UserIntent userIntent = new UserIntent();
		UserInput currentInput = new UserInput();
		Scanner scanner = new Scanner(System.in);
		MessageParserImpl messageParser = new MessageParserImpl();
		while(scanner.hasNextLine()){
			String s = scanner.nextLine();
			currentInput.setInputSentence(s);
			userIntent.setCurrentInput(currentInput);
			messageParser.parseInputMessage(userIntent);
		}
	}
}
