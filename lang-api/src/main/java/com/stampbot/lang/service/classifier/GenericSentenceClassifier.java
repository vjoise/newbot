package com.stampbot.lang.service.classifier;

import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.domain.SentenceTypeEnum;
import com.stampbot.lang.nlp.SentenceParser;
import com.stampbot.lang.service.trainer.feature.QuestionFeatureBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Scanner;

@Component
@Slf4j
public class GenericSentenceClassifier {

	FilteredClassifier classifier;

	@Autowired
	private SentenceParser sentenceParser;

	@Autowired
	QuestionFeatureBuilder featureBuilder;

	@PostConstruct
	public void loadClassifier() throws Exception {
		String fileName = "generic-sentence-classifier.dat";
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		Object tmp = in.readObject();
		classifier = (FilteredClassifier) tmp;
		log.info("Loaded the Generic Sentence Classifier from memory... :: " + fileName);
		in.close();
	}

	public SentenceTypeEnum classify(String sentence) throws Exception {
		InputSentence inputSentence = sentenceParser.parseInputMessage(sentence);
		return SentenceTypeEnum.byInternal(classify(featureBuilder.build(inputSentence)));
	}

	public String classify(Map<String, Object> features) throws Exception {
		// Create the attributes, class and text
		FastVector fvNominalVal = new FastVector(3);
		fvNominalVal.addElement("question");
		fvNominalVal.addElement("statement");
		fvNominalVal.addElement("none");
		Attribute attribute1 = new Attribute("sentenceclass", fvNominalVal);
		FastVector<String> attributeValues = new FastVector<>();
		Attribute attribute2 = new Attribute("NN");
		Attribute attribute3 = new Attribute("CD");
		Attribute attribute4 = new Attribute("wordCount");
		Attribute attribute5 = new Attribute("stemmedCount");
		Attribute attribute6 = new Attribute("NNPS");
		Attribute attribute7 = new Attribute("questionMarkCount");
		Attribute attribute8 = new Attribute("verbCombo");
		Attribute attribute9 = new Attribute("stemmedEndWithNoun");
		Attribute attribute10 = new Attribute("startTuple0");
		Attribute attribute11 = new Attribute("VBZ");
		Attribute attribute12 = new Attribute("PRP");
		Attribute attribute13 = new Attribute("endTuple0");
		Attribute attribute14 = new Attribute("NNP");
		Attribute attribute15 = new Attribute("NNS");
		Attribute attribute16 = new Attribute("verbBeforeNoun");
		Attribute attribute17 = new Attribute("VBG");
		Attribute attribute18 = new Attribute("qTripleScore");
		Attribute attribute19 = new Attribute("sTripleScore");
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(19);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		fvWekaAttributes.addElement(attribute3);
		fvWekaAttributes.addElement(attribute4);
		fvWekaAttributes.addElement(attribute5);
		fvWekaAttributes.addElement(attribute6);
		fvWekaAttributes.addElement(attribute7);
		fvWekaAttributes.addElement(attribute8);
		fvWekaAttributes.addElement(attribute9);
		fvWekaAttributes.addElement(attribute10);
		fvWekaAttributes.addElement(attribute11);
		fvWekaAttributes.addElement(attribute12);
		fvWekaAttributes.addElement(attribute13);
		fvWekaAttributes.addElement(attribute14);
		fvWekaAttributes.addElement(attribute15);
		fvWekaAttributes.addElement(attribute16);
		fvWekaAttributes.addElement(attribute17);
		fvWekaAttributes.addElement(attribute18);
		fvWekaAttributes.addElement(attribute19);
		Instances instances = new Instances("generic_question_test", fvWekaAttributes, 1);
		// Set class index
		DenseInstance instance = new DenseInstance(19);
		instances.setClassIndex(0);
		instance.setValue(attribute2, (int) features.get("NN"));
		instance.setValue(attribute3, (int) features.get("CD"));
		instance.setValue(attribute4, (int) features.get("wordCount"));
		instance.setValue(attribute5, (int) features.get("stemmedCount"));
		instance.setValue(attribute6, (int) features.get("NNPS"));
		instance.setValue(attribute7, (int) features.get("questionMarkCount"));
		instance.setValue(attribute8, (int) features.get("verbCombo"));
		instance.setValue(attribute9, (int) features.get("stemmedEndWithNoun"));
		instance.setValue(attribute10, (int) features.get("startTuple0"));
		instance.setValue(attribute11, (int) features.get("VBZ"));
		instance.setValue(attribute12, (int) features.get("PRP"));
		instance.setValue(attribute13, (int) features.get("endTuple0"));
		instance.setValue(attribute14, (int) features.get("NNP"));
		instance.setValue(attribute15, (int) features.get("NNS"));
		instance.setValue(attribute16, (int) features.get("verbBeforeNoun"));
		instance.setValue(attribute17, (int) features.get("VBG"));
		instance.setValue(attribute18, (long) features.get("qTripleScore"));
		instance.setValue(attribute19, (long) features.get("sTripleScore"));
		instances.add(instance);

		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);

		double pred = classifier.classifyInstance(instances.instance(0));
		return instances.classAttribute().value((int) pred);
	}


	public static void main(String[] args) throws Exception {
		GenericSentenceClassifier genericSentenceClassifier = new GenericSentenceClassifier();
		genericSentenceClassifier.loadClassifier();
		QuestionFeatureBuilder builder = new QuestionFeatureBuilder();
		SentenceParser sentenceParser = new SentenceParser();
		System.out.println("Enter input : ");
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Map<String, Object> build = builder.build(sentenceParser.parseInputMessage(line));
			System.out.println(genericSentenceClassifier.classify(build));
		}
	}
}
