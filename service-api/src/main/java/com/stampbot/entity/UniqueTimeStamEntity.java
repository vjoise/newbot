package com.stampbot.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@MappedSuperclass
@Data
public abstract class UniqueTimeStamEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String createdBy = "admin";

	@Column
	private String updatedBy = "admin";

	@Column
	private Calendar createdDate;

	@Column
	private Calendar updatedDate;

	@Column
	private String status;

	@PrePersist
	public void prePersist() {
		createdDate = updatedDate = Calendar.getInstance();
	}

	@PreUpdate
	public void preUpdate() {
		updatedDate = Calendar.getInstance();
	}

}
