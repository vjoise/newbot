package com.stampbot.service.nlp;

import com.stampbot.domain.UserInput;

public interface MessageParser {

    UserInput parseInputMessage(String inputMessage);

}
