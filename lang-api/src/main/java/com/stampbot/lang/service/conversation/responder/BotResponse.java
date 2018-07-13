package com.stampbot.lang.service.conversation.responder;

import lombok.Data;

@Data
public class BotResponse {

	private String data;

	private Boolean classified = false;

}
