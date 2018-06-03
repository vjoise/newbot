package com.stampbot.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class WorkflowEntity extends UniqueTimeStamEntity {

	@Column
	private String name;

	@OneToMany(mappedBy = "workflowEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<WorkflowQuestionEntity> questions;

}
