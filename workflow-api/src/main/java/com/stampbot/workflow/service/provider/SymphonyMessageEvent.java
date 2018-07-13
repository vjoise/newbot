package com.stampbot.workflow.service.provider;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class SymphonyMessageEvent extends ApplicationEvent {

	private String message;

	private Long userId;

	public SymphonyMessageEvent(Object source, Long userId, String message) {
		super(source);
		this.userId = userId;
		this.message = message;
	}

}
