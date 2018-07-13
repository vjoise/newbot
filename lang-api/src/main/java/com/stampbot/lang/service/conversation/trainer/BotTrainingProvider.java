package com.stampbot.lang.service.conversation.trainer;


public interface BotTrainingProvider {

	TrainingStatus train(String data);

}
