package com.stampbot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class WorkflowAnswerEntity extends UniqueTimeStamEntity{

	@Column
	private String responseText;

	@OneToOne
	@JoinColumn(name = "question_id")
	private WorkflowQuestionEntity question;


}
