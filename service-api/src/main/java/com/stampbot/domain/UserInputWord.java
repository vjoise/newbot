package com.stampbot.domain;

import lombok.Data;

@Data
public class UserInputWord {

    private String word;

    private String entity;

    private String pos;

    private String lemma;

    public UserInputWord(String word, String pos, String ne) {
        this.word = word;
        this.pos = pos;
        this.entity = ne;
    }
}
