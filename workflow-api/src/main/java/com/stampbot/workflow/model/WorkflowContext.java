package com.stampbot.workflow.model;

import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import lombok.Data;

@Data
public class WorkflowContext {

	private String conversationId;

	private Long userId;

	private String workflowName;

	private String inputSentence;

	private WorkflowQuestionEntity questionEntity;

	private String userIdMentionCSV;

	private boolean successFullyHandled = false;

}
