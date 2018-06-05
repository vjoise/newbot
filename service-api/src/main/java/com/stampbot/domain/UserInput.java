package com.stampbot.domain;

import com.stampbot.entity.UserWorkflowMasterEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import edu.stanford.nlp.ie.util.RelationTriple;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Data
@Component
@Scope("request")
public class UserInput {

    private String inputSentence;

    private Long userId;

    private List<UserInputWord> words = Lists.newArrayList();

    private boolean negativeSentiment = true;

    private RelationTriple relationTriple;

    private String actionHandlerClass;

    private String detectedWorkflow;

    private String conversationId;

    private WorkflowQuestionEntity questionEntity;

    private List<UserIdMention> userIdMentions;

    private UserWorkflowMasterEntity workflowMasterEntity;

    private UserInput linkedUserInput;

    private LocalDateTime timeStamp = LocalDateTime.now();

    public UserInput() {

    }

    public UserInput(String inputMessage) {
        this.inputSentence = inputMessage;
    }

    public void addWord(UserInputWord userInputWord) {
        this.words.add(userInputWord);
    }

}
