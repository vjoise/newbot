package com.stampbot.service.nlp.provider;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.service.nlp.MessageParser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
@Slf4j
class MessageParserImpl implements MessageParser {

    private static final Properties properties = new Properties();

    static {
        properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,regexner,parse,sentiment");
        properties.put("regexner.mapping", "regexner.txt");
    }

    @Override
    public UserInput parseInputMessage(String inputMessage) {
        UserInput userInput = new UserInput(inputMessage);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
        Annotation annotation = new Annotation(inputMessage);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        sentences.forEach(sentence -> {
            String sentimentPolarity = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            userInput.setNegativeSentiment("Negative".equalsIgnoreCase(sentimentPolarity.trim()));
            sentence.get(CoreAnnotations.TokensAnnotation.class).forEach(token -> {
                extractInputWord(userInput, token);
            });
            //printSemanticGraph(sentence);
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
        new MessageParserImpl().parseInputMessage("The task JIRA-1234 is not assigned to you");
    }
}
