package com.stampbot.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class UserWorkflowLog extends UniqueTimeStamEntity{

	@Column
	private Long userId;

	@Column
	private String conversationId;

	@Column
	private String questionId;

	@Column
	private Long workflowId;

	@Column
	private String detectedEntity;

	@Column
	private String inputText;

	@Column
	private Boolean passed;
}
