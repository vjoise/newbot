package com.stampbot.service.nlp;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserIntent;

public interface MessageParser {

    UserInput parseInputMessage(UserIntent intent);

}
