package com.stampbot.lang.service.trainer;

import org.springframework.core.io.ClassPathResource;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;

import java.util.Random;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.converters.ArffLoader.ArffReader;

import java.io.*;

public class WorkflowSentenceTrainer {

	Instances trainData;

	StringToWordVector filter;

	FilteredClassifier classifier;

	public void loadDataset(String fileName) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		ArffReader arff = new ArffReader(reader);
		trainData = arff.getData();
		System.out.println("===== Loaded dataset: " + fileName + " =====");
		reader.close();
	}

	public void evaluate() throws Exception {

		trainData.setClassIndex(0);
		filter = new StringToWordVector();
		filter.setAttributeIndices("last");
		classifier = new FilteredClassifier();
		classifier.setFilter(filter);
		classifier.setClassifier(new NaiveBayes());
		Evaluation eval = new Evaluation(trainData);
		eval.crossValidateModel(classifier, trainData, 4, new Random(1));
		System.out.println(eval.toSummaryString());
		System.out.println(eval.toClassDetailsString());
		System.out.println("===== Evaluating on filtered (training) dataset done =====");
	}

	/**
	 * This method trains the classifier on the loaded dataset.
	 */
	public void learn() throws Exception {
		trainData.setClassIndex(0);
		filter = new StringToWordVector();
		filter.setAttributeIndices("last");
		classifier = new FilteredClassifier();
		classifier.setFilter(filter);
		classifier.setClassifier(new NaiveBayes());
		classifier.buildClassifier(trainData);
	}

	public void saveModel(String fileName) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(classifier);
		out.close();
		System.out.println("===== Saved model: " + fileName + " =====");
	}

	public static void main(String[] args) throws Exception {
		WorkflowSentenceTrainer learner = new WorkflowSentenceTrainer();
		learner.loadDataset(new ClassPathResource("generic_training.arff").getURL().getPath());
		learner.evaluate();
		learner.learn();
		learner.saveModel("workflow-classifier.dat");
	}
}