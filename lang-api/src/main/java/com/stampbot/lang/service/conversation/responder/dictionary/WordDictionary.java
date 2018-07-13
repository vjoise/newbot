package com.stampbot.lang.service.conversation.responder.dictionary;

import com.stampbot.lang.service.corpus.CorpusProcessor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Data
public class WordDictionary {

	/**
	 * The contents of the corpus. This is a list of sentences (each word of the
	 * sentence is denoted by a {@link Double}).
	 */
	private final List<List<Double>> corpus = new ArrayList<>();

	/**
	 * Dictionary that maps words into numbers.
	 */
	private final Map<String, Double> dict = new HashMap<>();
	private final String CHARS = "-\\/_&" + CorpusProcessor.SPECIALS;
	private final Map<Double, String> revDict = new HashMap<>();
	private static final String CORPUS_FILENAME = "dialog.txt"; // filename of data corpus to learn
	private static final int MAX_DICT = 20000; // this number of most frequent words will be used, unknown words (that are not in the

	private static final int ROW_SIZE = 40; // maximum line length in tokens

	@PostConstruct
	public void createDictionary() throws Exception {
		double idx = 3.0;
		dict.put("<unk>", 0.0);
		revDict.put(0.0, "<unk>");
		dict.put("<eos>", 1.0);
		revDict.put(1.0, "<eos>");
		dict.put("<go>", 2.0);
		revDict.put(2.0, "<go>");
		for (char c : CHARS.toCharArray()) {
			if (!dict.containsKey(c)) {
				dict.put(String.valueOf(c), idx);
				revDict.put(idx, String.valueOf(c));
				++idx;
			}
		}
		System.out.println("Building the dictionary...");
		CorpusProcessor corpusProcessor = new CorpusProcessor((CORPUS_FILENAME), ROW_SIZE, true);
		corpusProcessor.start();
		Map<String, Double> freqs = corpusProcessor.getFreq();
		Set<String> dictSet = new TreeSet<>(); // the tokens order is preserved for TreeSet
		Map<Double, Set<String>> freqMap = new TreeMap<>(new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				return (int) (o2 - o1);
			}
		}); // tokens of the same frequency fall under the same key, the order is reversed so the most frequent tokens go first
		for (Map.Entry<String, Double> entry : freqs.entrySet()) {
			Set<String> set = freqMap.get(entry.getValue());
			if (set == null) {
				set = new TreeSet<>(); // tokens of the same frequency would be sorted alphabetically
				freqMap.put(entry.getValue(), set);
			}
			set.add(entry.getKey());
		}
		int cnt = 0;
		dictSet.addAll(dict.keySet());
		// get most frequent tokens and put them to dictSet
		for (Map.Entry<Double, Set<String>> entry : freqMap.entrySet()) {
			for (String val : entry.getValue()) {
				if (dictSet.add(val) && ++cnt >= MAX_DICT) {
					break;
				}
			}
			if (cnt >= MAX_DICT) {
				break;
			}
		}
		// all of the above means that the dictionary with the same MAX_DICT constraint and made from the same source file will always be
		// the same, the tokens always correspond to the same number so we don't need to save/restore the dictionary
		System.out.println("Dictionary is ready, size is " + dictSet.size());
		// index the dictionary and build the reverse dictionary for lookups
		for (String word : dictSet) {
			if (!dict.containsKey(word)) {
				dict.put(word, idx);
				revDict.put(idx, word);
				++idx;
			}
		}
		System.out.println("Total dictionary size is " + dict.size() + ". Processing the dataset...");
		corpusProcessor = new CorpusProcessor((CORPUS_FILENAME), ROW_SIZE, false) {
			@Override
			protected void processLine(String lastLine) {
				List<String> words = new ArrayList<>();
				tokenizeLine(lastLine, words, true);
				corpus.add(wordsToIndexes(words));
			}
		};
		corpusProcessor.setDict(dict);
		corpusProcessor.start();
		System.out.println("Done. Corpus size is " + corpus.size());
	}

	public int size() {
		return dict.size();
	}

	public String get(double s) {
		return revDict.get(s);
	}

	public Map<String,Double> getInternal() {
		return dict;
	}
}
