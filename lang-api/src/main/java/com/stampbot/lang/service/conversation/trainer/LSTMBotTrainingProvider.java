package com.stampbot.lang.service.conversation.trainer;

import com.stampbot.lang.service.conversation.responder.dictionary.WordDictionary;
import com.stampbot.lang.service.corpus.CorpusIterator;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.graph.rnn.DuplicateToTimeSeriesVertex;
import org.deeplearning4j.nn.conf.graph.rnn.LastTimeStepVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
class LSTMBotTrainingProvider implements BotTrainingProvider {

	private static final int HIDDEN_LAYER_WIDTH = 16; // this is purely empirical, affects performance and VRAM requirement
	private static final int EMBEDDING_WIDTH = 128; // one-hot vectors will be embedded to more dense vectors with this width
	private static final String BACKUP_MODEL_FILENAME = "rnn_train.bak.zip"; // filename of the previous version of the model (backup)
	private static final int MINIBATCH_SIZE = 32;
	// dictionary) are replaced with <unk> token
	private static final int TBPTT_SIZE = 25;
	private static final double LEARNING_RATE = 1e-1;
	private static final double RMS_DECAY = 0.95;

	private static final int MACROBATCH_SIZE = 20; // see CorpusIterator
	private static final int ROW_SIZE = 40; // maximum line length in tokens

	@Autowired
	private WordDictionary dictionary = new WordDictionary();

	private ComputationGraph net;

	@PostConstruct
	public void initialize() throws Exception {
		dictionary.createDictionary();
		createComputationGraph();
		File file = new File(MODEL_FILENAME);
		log.info("Training in progress...");
		if (!file.exists())
			train(file);
		log.info("Training the LSTM model complete...");
	}

	private static final String MODEL_FILENAME = "c:\\users\\deprasa\\Desktop\\hackathon\\rnn_train.zip"; // filename of the model

	@Override
	public TrainingStatus train(String data) {
		try {

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private void train(File networkFile) throws IOException {
		CorpusIterator logsIterator = new CorpusIterator(dictionary.getCorpus(), MINIBATCH_SIZE, MACROBATCH_SIZE, dictionary.size(), ROW_SIZE);
		for (int epoch = 1; epoch < 500; ++epoch) {
			System.out.println("Epoch " + epoch);
			if (epoch == 1) {
				logsIterator.setCurrentBatch(0);
			} else {
				logsIterator.reset();
			}
			int lastPerc = 0;
			while (logsIterator.hasNextMacrobatch()) {
				net.fit(logsIterator);
				logsIterator.nextMacroBatch();
				System.out.println("Batch = " + logsIterator.batch());
				int newPerc = (logsIterator.batch() * 100 / logsIterator.totalBatches());
				if (newPerc != lastPerc) {
					System.out.println("Epoch complete: " + newPerc + "%");
					lastPerc = newPerc;
				}
				saveModel(networkFile);
			}
		}
	}

	private void saveModel(File networkFile) throws IOException {
		System.out.println("Saving the model...");
		File backup = new File(BACKUP_MODEL_FILENAME);
		if (networkFile.exists()) {
			if (backup.exists()) {
				backup.delete();
			}
			networkFile.renameTo(backup);
		}
		ModelSerializer.writeModel(net, networkFile, true);
		log.info("Done.");
	}

	private void createComputationGraph() {
		final NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder()
				.updater(new RmsProp(LEARNING_RATE))
				.weightInit(WeightInit.XAVIER)
				.gradientNormalization(GradientNormalization.RenormalizeL2PerLayer);

		final ComputationGraphConfiguration.GraphBuilder graphBuilder = builder.graphBuilder()
				.pretrain(false)
				.backprop(true)
				.backpropType(BackpropType.Standard)
				.tBPTTBackwardLength(TBPTT_SIZE)
				.tBPTTForwardLength(TBPTT_SIZE)
				.addInputs("inputLine", "decoderInput")
				.setInputTypes(InputType.recurrent(dictionary.size()), InputType.recurrent(dictionary.size()))
				.addLayer("embeddingEncoder", new EmbeddingLayer.Builder().nIn(dictionary.size()).nOut(EMBEDDING_WIDTH).build(), "inputLine")
				.addLayer("encoder", new LSTM.Builder().nIn(EMBEDDING_WIDTH).nOut(HIDDEN_LAYER_WIDTH).activation(Activation.TANH).build(), "embeddingEncoder")
				.addVertex("thoughtVector", new LastTimeStepVertex("inputLine"), "encoder")
				.addVertex("dup", new DuplicateToTimeSeriesVertex("decoderInput"), "thoughtVector")
				.addVertex("merge", new MergeVertex(), "decoderInput", "dup")
				.addLayer("decoder", new LSTM.Builder().nIn(dictionary.size() + HIDDEN_LAYER_WIDTH).nOut(HIDDEN_LAYER_WIDTH).activation(Activation.TANH).build(), "merge")
				.addLayer("output", new RnnOutputLayer.Builder().nIn(HIDDEN_LAYER_WIDTH).nOut(dictionary.size()).activation(Activation.SOFTMAX).lossFunction(LossFunctions.LossFunction.MCXENT).build(), "decoder")
				.setOutputs("output");

		net = new ComputationGraph(graphBuilder.build());
		net.init();
	}

	public static void main(String[] args) throws Exception{
		new LSTMBotTrainingProvider().initialize();
	}
}
