package com.stampbot.domain;

import com.google.common.collect.Lists;
import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import lombok.Data;
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

	private Integer numberOfWrongInputs = 0;

	public void incrementWrongInputAttempts(){
		this.numberOfWrongInputs++;
	}

	public void addInput(UserInput userInput){
		currentInput = userInput;
		userInputList.add(currentInput);
	}

}
