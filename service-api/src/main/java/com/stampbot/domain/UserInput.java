package com.stampbot.domain;

import com.google.common.collect.Lists;
import com.stampbot.workflow.entity.UserWorkflowMasterEntity;
import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import com.stampbot.lang.domain.SentenceRelation;
import edu.stanford.nlp.ie.util.RelationTriple;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

	private SentenceRelation sentenceRelation;

	public UserInput() {

    }

    public UserInput(String inputMessage) {
        this.inputSentence = inputMessage;
    }

    public void addWord(UserInputWord userInputWord) {
        this.words.add(userInputWord);
    }

    private List<String> ngramTokensList = Lists.newArrayList();

    public void addToNGramTokensList(String s) {
        ngramTokensList.add(s);
    }

}
