package com.stampbot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ActionItemEntity extends UniqueTimeStamEntity {

	@Column
	private String action;

	@Column
	private String resource;

	@Column
	private Long userId;

	@Column
	private String status;

	@OneToMany
	private List<ActionItemDetailEntity> itemDetails;

}
