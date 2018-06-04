package com.stampbot.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class UserBotConversationLog extends UniqueTimeStamEntity{

	@Column
	private Long userId;

	@Column
	private String userInput;

	@Column
	private String conversationId;

	@Column
	private String botRepsonse;

	@Column
	private Long workflowId;

}
