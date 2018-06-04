package com.stampbot.domain;

import com.stampbot.entity.WorkflowQuestionEntity;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.Data;
import org.apache.lucene.queryparser.xml.builders.UserInputQueryBuilder;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Data
@Scope("session")
public class UserIntent {

	private String conversationId;

	List<UserInput> userInputList = Lists.newArrayList();

	private boolean answered;

	private Long userId;

	private WorkflowQuestionEntity unansweredQuestion;

	boolean noCourtesyMessageSent;

	private UserInput currentInput;

	public void addInput(UserInput userInput){
		currentInput = userInput;
		userInputList.add(currentInput);
	}

}
