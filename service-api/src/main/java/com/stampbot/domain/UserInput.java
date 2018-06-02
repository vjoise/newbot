package com.stampbot.domain;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Scope("request")
public class UserInput {

    private String inputSentence;

    private List<UserInputWord> words = Lists.newArrayList();

    private boolean negativeSentiment = true;

    public UserInput() {

    }

    public UserInput(String inputMessage) {
        this.inputSentence = inputMessage;
    }

    public void addWord(UserInputWord userInputWord) {
        this.words.add(userInputWord);
    }

}
