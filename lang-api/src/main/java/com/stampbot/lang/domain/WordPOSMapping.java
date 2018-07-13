package com.stampbot.lang.domain;

import lombok.Data;

@Data
public class WordPOSMapping {

	private String pos;

	private String word;

	private String entity;

	private String lemma;

	public WordPOSMapping(String word, String pos, String entity) {
		this.word = word;
		this.pos = pos;
		this.entity = entity;
	}
}
