package com.stampbot.service.symphony.service;

import com.stampbot.workflow.service.provider.SymphonyMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.stampbot.common.Utils.trySafe;

@Component
public class SymphonyMessageEventListener implements ApplicationListener<SymphonyMessageEvent> {

	@Autowired
	private SymphonyService symphonyService;

	@Override
	public void onApplicationEvent(SymphonyMessageEvent event) {
		trySafe(() -> symphonyService.sendMessage(event.getUserId(), event.getMessage()), true);
	}
}
