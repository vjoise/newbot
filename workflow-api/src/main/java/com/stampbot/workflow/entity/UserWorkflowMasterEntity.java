package com.stampbot.workflow.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class UserWorkflowMasterEntity extends UniqueTimeStamEntity{

	@Column
	private Long userId;

	@Column
	private String conversationId;

	@Column
	private String status;

	@OneToMany(mappedBy = "workflowMaster", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<UserWorkflowLogEntity> userWorkflowLogEntities;

}
