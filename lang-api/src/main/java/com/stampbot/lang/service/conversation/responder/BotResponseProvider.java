package com.stampbot.lang.service.conversation.responder;

import com.stampbot.lang.domain.InputSentence;

public interface BotResponseProvider {

	BotResponse getUserResponse(InputSentence sentence) throws Exception;

}
