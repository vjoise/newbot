package com.stampbot.workflow.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowQuestionEntity extends UniqueTimeStamEntity {

	@Column
	private String questionText;

	@Column
	private String questionKey;

	@OneToOne
	@JoinColumn(name = "next_question")
	private WorkflowQuestionEntity nextQuestion;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private WorkflowEntity workflowEntity;

	@OneToOne(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private WorkflowAnswerEntity answer;

	@Column
	private String actionHandler;

	@Column
	private String inputValidator;

	public WorkflowQuestionEntity() {
	}
}
