package com.stampbot.lang.service.trainer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.Random;

@Component
@Slf4j
public class GenericSentenceTrainer {

	Instances trainData;

	FilteredClassifier classifier;

	public void loadDataset(String fileName) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
		trainData = arff.getData();
		log.info("===== Loaded dataset: " + fileName + " =====");
		reader.close();
	}

	public void evaluate() throws Exception {

		trainData.setClassIndex(0);
		StringToWordVector filter = new StringToWordVector();
		filter.setAttributeIndices("1-last");
		classifier = new FilteredClassifier();
		classifier.setFilter(filter);
		classifier.setClassifier(new RandomForest());
		Evaluation eval = new Evaluation(trainData);
		eval.crossValidateModel(classifier, trainData, 4, new Random(1));
		log.info(eval.toSummaryString());
		log.info(eval.toClassDetailsString());
		log.info("===== Evaluating on filtered (training) dataset done =====");
	}

	public void learn() throws Exception {
		trainData.setClassIndex(0);
		classifier = new FilteredClassifier();
		StringToWordVector filter = new StringToWordVector();
		filter.setAttributeIndices("1-last");
		classifier.setFilter(filter);
		classifier.setClassifier(new RandomForest());
		classifier.buildClassifier(trainData);
	}

	public void saveModel(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(classifier);
		out.close();
		log.info("===== Saved model: " + fileName + " =====");
	}

	public static void main(String[] args) throws Exception {
		GenericSentenceTrainer learner = new GenericSentenceTrainer();
		learner.loadDataset(new ClassPathResource("generic-sentence-training.arrf").getURL().getPath());
		learner.evaluate();
		learner.learn();
		learner.saveModel("generic-sentence-classifier.dat");
	}

}
