package com.stampbot.lang.service.conversation.responder;

import com.stampbot.lang.service.conversation.trainer.BotTrainingProvider;
import com.stampbot.lang.service.corpus.CorpusProcessor;
import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.service.conversation.responder.dictionary.WordDictionary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.graph.vertex.GraphVertex;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
class LSTMBotResponseProvider implements BotResponseProvider {

	private static final int GC_WINDOW = 2000;

	private static final String MODEL_FILENAME = "c:\\users\\deprasa\\Desktop\\hackathon\\rnn_train.zip"; // filename of the model
	private static final Random rnd = new Random(new Date().getTime());
	private static final int ROW_SIZE = 40; // maximum line length in tokens

	/**
	 * The computation graph model.
	 */
	private ComputationGraph net;

	@Autowired
	private WordDictionary dictionary;

	@Autowired
	private BotTrainingProvider trainingProvider;

	@PostConstruct
	public void init() throws Exception {
		Nd4j.getMemoryManager().setAutoGcWindow(GC_WINDOW);
		File file = new File(MODEL_FILENAME);
		log.info("File Exists :: " + file.exists());
		net = ModelSerializer.restoreComputationGraph(file);
	}

	private String output(List<Double> rowIn, boolean printUnknowns) {
		StringBuilder builder = new StringBuilder();
		net.rnnClearPreviousState();
		Collections.reverse(rowIn);
		INDArray in = Nd4j.create(ArrayUtils.toPrimitive(rowIn.toArray(new Double[0])), new int[]{1, 1, rowIn.size()});
		double[] decodeArr = new double[dictionary.size()];
		decodeArr[2] = 1;
		INDArray decode = Nd4j.create(decodeArr, new int[]{1, dictionary.size(), 1});
		net.feedForward(new INDArray[]{in, decode}, false, false);
		org.deeplearning4j.nn.layers.recurrent.LSTM decoder = (org.deeplearning4j.nn.layers.recurrent.LSTM) net
				.getLayer("decoder");
		Layer output = net.getLayer("output");
		GraphVertex mergeVertex = net.getVertex("merge");
		INDArray thoughtVector = mergeVertex.getInputs()[1];
		LayerWorkspaceMgr mgr = LayerWorkspaceMgr.noWorkspaces();
		for (int row = 0; row < ROW_SIZE; ++row) {
			mergeVertex.setInputs(decode, thoughtVector);
			INDArray merged = mergeVertex.doForward(false, mgr);
			INDArray activateDec = decoder.rnnTimeStep(merged, mgr);
			INDArray out = output.activate(activateDec, false, mgr);
			double d = rnd.nextDouble();
			double sum = 0.0;
			int idx = -1;
			for (int s = 0; s < out.size(1); s++) {
				sum += out.getDouble(0, s, 0);
				if (d <= sum) {
					idx = s;
					if (printUnknowns || s != 0) {
						builder.append(dictionary.get((double)s)).append(" ");
					}
					break;
				}
			}
			if (idx == 1) {
				break;
			}
			double[] newDecodeArr = new double[dictionary.size()];
			newDecodeArr[idx] = 1;
			decode = Nd4j.create(newDecodeArr, new int[]{1, dictionary.size(), 1});
		}
		System.out.println();
		return builder.toString();
	}

	private BotResponse getOutput(String inputSentence) throws IOException {
		BotResponse botResponse = new BotResponse();
		final String[] output = {""};
		System.out.println("Dialog started.");
		String line = "1 +++$+++ u0 +++$+++ m0 +++$+++ USER +++$+++ " + inputSentence + "\n";
		CorpusProcessor dialogProcessor = new CorpusProcessor(new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8)), ROW_SIZE,
				false) {
			@Override
			protected void processLine(String lastLine) {
				List<String> words = new ArrayList<>();
				tokenizeLine(lastLine, words, true);
				final List<Double> wordIdxs = wordsToIndexes(words);
				int numberOfMatchingDictionaryWords = 0;
				if (!wordIdxs.isEmpty()) {
					System.out.print("Got words: ");
					for (Double idx : wordIdxs) {
						String s = dictionary.get(idx);
						if (!StringUtils.equals("<unk>", s)) {
							numberOfMatchingDictionaryWords++;
						}
						System.out.print(s + " ");
					}
					if(numberOfMatchingDictionaryWords / (float)words.size()  > 0.8){
						botResponse.setClassified(true);
					}
					System.out.println();
					System.out.print("Out> ");
					String stringOutput = output(wordIdxs, true);
					botResponse.setData(stringOutput.replace("<eos>", ""));
				}
			}
		};
		dialogProcessor.setDict(dictionary.getInternal());
		dialogProcessor.start();
		return botResponse;
	}

	@Override
	public BotResponse getUserResponse(InputSentence sentence) throws Exception {
		return getOutput(sentence.getSentence());
	}


}
