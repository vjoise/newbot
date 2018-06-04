package com.stampbot.service.symphony.feed;

import com.google.common.collect.Maps;
import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserIntent;
import com.stampbot.entity.UserBotConversationLog;
import com.stampbot.repository.UserBotConversationRepository;
import com.stampbot.service.nlp.MessageParser;
import com.stampbot.service.nlp.classifier.ClassifierResponse;
import com.stampbot.service.nlp.classifier.ClassifierResponseType;
import com.stampbot.service.nlp.classifier.UserInputClassifier;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.workflow.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.events.SymEvent;
import org.symphonyoss.symphony.clients.DataFeedClient;
import org.symphonyoss.symphony.clients.model.ApiVersion;
import org.symphonyoss.symphony.clients.model.SymDatafeed;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private Map<String, UserIntent> userIntentMap = Maps.newHashMap();

	@Autowired
	private UserBotConversationRepository userBotConversationRepository;

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
			UserIntent userIntent = extractUserIntent(symEvent);
			persistUserConversationLog(symEvent, null);
			ClassifierResponse classifiedResponse = userInputClassifier.classify(messageText);
			UserInput userInput = messageParser.parseInputMessage(userIntent);
			final String[] botResponse = {""};
			if (!userInput.isNegativeSentiment()) {
				if (classifiedResponse.getResponseType() == ClassifierResponseType.WORKFLOW) {
					HashMap<String, Object> workflowContext = triggerWorkflow(symEvent, classifiedResponse, userInput);
					botResponse[0] = String.class.cast(workflowContext.get("botResponse"));
				}
			} else {
				trySafe(() -> {
					botResponse[0] = "Sorry didn't get that!";
					symphonyService.sendMessage(symEvent, botResponse[0]);
				}, true);
			}
			persistUserConversationLog(symEvent, botResponse[0]);
		});
	}

	private HashMap<String, Object> triggerWorkflow(SymEvent symEvent, ClassifierResponse classifiedResponse, UserInput userInput) {
		userInput.setDetectedWorkflow(classifiedResponse.getMessage());
		HashMap<String, Object> workflowContext = Maps.newHashMap();
		workflowContext.put("userInput", userInput);
		workflowContext.put("symEvent", symEvent);
		workflowService.process(workflowContext);
		return workflowContext;
	}

	private void persistUserConversationLog(SymEvent symEvent, String botResponse) {
		String conversationId = symEvent.getPayload().getMessageSent().getStreamId();
		Long userId = symEvent.getInitiator().getId();
		UserBotConversationLog log = userBotConversationRepository.findByConversationIdAndUserId(conversationId, userId);
		if (log == null) {
			log = new UserBotConversationLog();
		}
		log.setUserId(symEvent.getInitiator().getId());
		log.setUserInput(symEvent.getPayload().getMessageSent().getMessageText());
		log.setConversationId(symEvent.getPayload().getMessageSent().getStreamId());
		log.setBotRepsonse(botResponse);
		userBotConversationRepository.save(log);
	}

	private UserIntent extractUserIntent(SymEvent symEvent) {
		UserIntent userIntent = userIntentMap.get(symEvent.getInitiator().getEmailAddress());
		if (userIntent == null) {
			userIntent = new UserIntent();
		}
		userIntent.addInput(new UserInput(symEvent.getPayload().getMessageSent().getMessageText()));
		return userIntent;
	}

	private boolean isUserBot(SymEvent symEvent) {
		return symEvent.getInitiator().getDisplayName().startsWith("bot.user");
	}


}
