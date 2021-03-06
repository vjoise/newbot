package com.stampbot.workflow.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserWorkflowLogEntity extends UniqueTimeStamEntity{

	@Column
	private Long userId;

	@Column
	private String conversationId;

	@Column
	private Long questionId;

	@Column
	private Long workflowId;

	@Column
	private String detectedEntity;

	@Column
	private String inputText;

	@Column
	private String userMentionIdsList;

	@Column
	private Boolean passed;

	@Column
	private String status;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private UserWorkflowMasterEntity workflowMaster;
}
