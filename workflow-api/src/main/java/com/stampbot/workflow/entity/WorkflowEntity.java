package com.stampbot.workflow.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class WorkflowEntity extends UniqueTimeStamEntity {

	@Column
	private String name;

	@OneToMany(mappedBy = "workflowEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<WorkflowQuestionEntity> questions;

}
