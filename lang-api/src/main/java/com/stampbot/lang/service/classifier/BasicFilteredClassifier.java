package com.stampbot.lang.service.classifier;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

@Component
@Slf4j
public class BasicFilteredClassifier {

	FilteredClassifier classifier;

	@PostConstruct
	public void loadModel() throws Exception {
		String fileName = "basic-classifier.dat";
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		Object tmp = in.readObject();
		classifier = (FilteredClassifier) tmp;
		log.info("Loaded the classifier from memory... :: " + fileName);
		in.close();
	}

	public String classify(String inputSentence) throws Exception {
		// Create the attributes, class and text
		FastVector fvNominalVal = new FastVector(2);
		fvNominalVal.addElement("user_workflow");
		fvNominalVal.addElement("dev_workflow");
		fvNominalVal.addElement("none");
		Attribute attribute1 = new Attribute("class", fvNominalVal);
		Attribute attribute2 = new Attribute("text", (FastVector) null);
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		Instances instances = new Instances("Test relation", fvWekaAttributes, 1);
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute2, inputSentence);
		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance);
		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
		double pred = classifier.classifyInstance(instances.instance(0));
		return instances.classAttribute().value((int) pred);
	}

	public static void main(String[] args) throws Exception {
		BasicFilteredClassifier classifier = new BasicFilteredClassifier();
		//classifier.loadModel("basic-classifier.dat");
		System.out.println(classifier.classify("please test"));
	}
}