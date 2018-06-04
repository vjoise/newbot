package com.stampbot.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ActionItemDetailEntity extends UniqueTimeStamEntity {

	@Column
	private Long userId;

	@Column
	private String actionId;


}
