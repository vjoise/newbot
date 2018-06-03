package com.stampbot.service.symphony.feed;

import com.google.common.collect.Maps;
import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.model.IssueResponse;
import com.stampbot.service.nlp.MessageParser;
import com.stampbot.service.nlp.classifier.UserInputClassifier;
import com.stampbot.service.nlp.classifier.ClassifierResponse;
import com.stampbot.service.nlp.classifier.ClassifierResponseType;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.task.TaskService;
import com.stampbot.service.workflow.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.events.SymEvent;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.exceptions.StreamsException;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.symphony.clients.DataFeedClient;
import org.symphonyoss.symphony.clients.StreamsClient;
import org.symphonyoss.symphony.clients.model.ApiVersion;
import org.symphonyoss.symphony.clients.model.SymDatafeed;
import org.symphonyoss.symphony.clients.model.SymMessage;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.stampbot.common.Utils.trySafe;

@Slf4j
@Component
public class DataFeedProcessor {

	@Autowired
	private SymphonyClient symphonyClient;

	private DataFeedClient dataFeedClient;

	private SymDatafeed dataFeed;

	@Autowired
	private MessageParser messageParser;

	@Autowired
	private UserInputClassifier userInputClassifier;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private SymphonyService symphonyService;

	@PostConstruct
	public void init() throws Exception {
		log.info("Initializing the DataFeedClient");
		dataFeedClient = symphonyClient.getDataFeedClient();
		dataFeed = dataFeedClient.createDatafeed(ApiVersion.V4);
		log.info("DataFeedClient ID :: " + dataFeedClient);
		log.info("Data Feed ID :: " + dataFeed.getId());
	}

	@Scheduled(fixedRate = 1000)
	public void processIncomingMessage() throws Exception {
		log.info("Checking for incoming user chat messages...");
		List<SymEvent> eventsFromDatafeed = dataFeedClient.getEventsFromDatafeed(dataFeed);
		if (eventsFromDatafeed == null) {
			return;
		}
		log.info("Received user chat messages :: " + eventsFromDatafeed.size());
		eventsFromDatafeed.forEach(symEvent -> {
			if (isUserBot(symEvent)) {
				return;
			}
			String messageText = symEvent.getPayload().getMessageSent().getMessageText();
			log.info("Message from user  :: " + messageText);
			ClassifierResponse classifiedResponse = userInputClassifier.classify(messageText);
			UserInput userInput = messageParser.parseInputMessage(messageText);
			if (!userInput.isNegativeSentiment()) {
				if (classifiedResponse.getResponseType() == ClassifierResponseType.WORKFLOW) {
					userInput.setDetectedWorkflow(classifiedResponse.getMessage());
					HashMap<String, Object> workflowContext = Maps.newHashMap();
					workflowContext.put("userInput", userInput);
					workflowContext.put("symEvent", symEvent);
					workflowService.process(workflowContext);
				}
			}else{
				trySafe(() -> {
					symphonyService.sendMessage(symEvent, "Sorry didn't get that!");
				}, true);
			}
		});
	}

	private boolean isUserBot(SymEvent symEvent) {
		return symEvent.getInitiator().getDisplayName().startsWith("bot.user");
	}


}
