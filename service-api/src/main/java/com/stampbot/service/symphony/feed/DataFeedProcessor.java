package com.stampbot.service.symphony.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.stampbot.config.StampBotConfig;
import com.stampbot.domain.UserIdMention;
import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserIntent;
import com.stampbot.entity.UserBotConversationLog;
import com.stampbot.entity.UserWorkflowLogEntity;
import com.stampbot.entity.WorkflowQuestionEntity;
import com.stampbot.repository.UserBotConversationRepository;
import com.stampbot.repository.WorkflowQuestionRepository;
import com.stampbot.service.nlp.MessageParser;
import com.stampbot.service.nlp.classifier.ClassifierResponse;
import com.stampbot.service.nlp.classifier.ClassifierResponseType;
import com.stampbot.service.nlp.classifier.UserInputClassifier;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.service.workflow.UserWorkflowStore;
import com.stampbot.service.workflow.WorkflowService;
import com.stampbot.service.workflow.handler.WorkflowQuestionValidator;
import com.stampbot.util.UserUtil;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.events.SymEvent;
import org.symphonyoss.symphony.clients.DataFeedClient;
import org.symphonyoss.symphony.clients.model.ApiVersion;
import org.symphonyoss.symphony.clients.model.SymDatafeed;
import org.symphonyoss.symphony.clients.model.SymMessage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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

	@Autowired
	private UserBotConversationRepository userBotConversationRepository;

	@Autowired
	private UserWorkflowStore userWorkflowStore;

	@Autowired
	private WorkflowQuestionRepository questionRepository;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private StampBotConfig config;

	private static final Map<String, UserRoleEnum> USER_ROLE_MAP = Maps.newHashMap();
	private static final Map<UserRoleEnum, String> USER_WORKFLOW_MAP = Maps.newHashMap();

	@PostConstruct
	public void init() throws Exception {
		log.info("Initializing the DataFeedClient");
		dataFeedClient = symphonyClient.getDataFeedClient();
		dataFeed = dataFeedClient.createDatafeed(ApiVersion.V4);
		log.info("DataFeedClient ID :: " + dataFeedClient);
		log.info("Data Feed ID :: " + dataFeed.getId());
		USER_ROLE_MAP.put("jingyu.li@credit-suisse.com", UserRoleEnum.DEV);
		USER_ROLE_MAP.put("venkatesh.joisekrishnamurthy@credit-suisse.com", UserRoleEnum.USER);
		USER_WORKFLOW_MAP.put(UserRoleEnum.DEV, "DEV_WORKFLOW");
		USER_WORKFLOW_MAP.put(UserRoleEnum.USER, "USER_WORKFLOW");
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
			SymMessage messageSent = symEvent.getPayload().getMessageSent();
			List<UserIdMention> mentionedUserIds = Lists.newArrayList();

			UserUtil.addUser(symEvent.getInitiator());

			if (StringUtils.isNotBlank(messageSent.getEntityData())) {
				checkAndPopulateUserMentions(messageSent, mentionedUserIds);
			}
			String messageText = messageSent.getMessageText();
			log.info("Message from user  :: " + messageText);
			UserIntent userIntent = extractUserIntent(symEvent);
			persistUserConversationLog(symEvent, null);
			UserInput userInput = messageParser.parseInputMessage(userIntent);
			userInput.setUserIdMentions(mentionedUserIds);
			userInput.setUserId(symEvent.getInitiator().getId());
			userInput.setConversationId(messageSent.getStreamId());

			boolean noPendingAnswersFromThisUser = userWorkflowStore.isEmpty(userInput);
			final String[] botResponse = {""};
			boolean nothingOtherThanGreeting = userInput.getWords()
					.stream()
					.filter(word -> StringUtils.isBlank(word.getEntity()) || !word.getEntity().equalsIgnoreCase("GREETING"))
					.count() == 0;
			if (nothingOtherThanGreeting) {
				trySafe(() -> {
					botResponse[0] = userInput.getInputSentence() + " " + symEvent.getInitiator().getDisplayName();
					symphonyService.sendMessage(symEvent, botResponse[0]);
				}, true);
				return;
			}
			if (noPendingAnswersFromThisUser) {
				if (true) {
					ClassifierResponse classifiedResponse = userInputClassifier.classify(messageText);
					String userEmailId = symEvent.getInitiator().getEmailAddress();
					if (userRoleNotMatchingWorkflow(userEmailId, classifiedResponse.getMessage())) {
						trySafe(() -> {
							botResponse[0] = "Sorry didn't get that!";
							symphonyService.sendMessage(symEvent, botResponse[0]);
						}, true);
					}
					processUserInput(symEvent, classifiedResponse, userInput, botResponse);
				} else {
					trySafe(() -> {
						botResponse[0] = "Sorry didn't get that!";
						symphonyService.sendMessage(symEvent, botResponse[0]);
					}, true);
				}
			} else {
				//he can also answer previous questions like yes/no for confirmation
				UserWorkflowLogEntity workflowLog = userWorkflowStore.findNextUnansweredQuestion(userInput.getConversationId());
				WorkflowQuestionEntity unansweredQuestion = questionRepository.findOne(workflowLog.getQuestionId());
				Class<?>[] validatorClass = {null};
				String inputValidator = unansweredQuestion.getInputValidator();
				workflowService.stringToClass(inputValidator, validatorClass);
				if (validatorClass[0] != null) {
					WorkflowQuestionValidator validator = WorkflowQuestionValidator.class.cast(context.getBean(validatorClass[0]));
					if (validator.isValidInput(userInput)) {
						//do this only if the input is valid.
						ClassifierResponse classifierResponse = new ClassifierResponse();
						classifierResponse.setMessage(unansweredQuestion.getWorkflowEntity().getName());
						classifierResponse.setResponseType(ClassifierResponseType.WORKFLOW);
						processUserInput(symEvent, classifierResponse, userInput, botResponse);
						boolean workflowEnded = userWorkflowStore.isEmpty(userInput);
						if (workflowEnded) {
							UserUtil.putIntent(userInput.getUserId(), null);
						}
					} else {
						if (userIntent.getNumberOfWrongInputs() >= Integer.parseInt(config.getMaxAllowedWrongInputs())) {
							trySafe(() -> {
								botResponse[0] = "I need a confirmation but you have exceeded the number of attempts, see you later!";
								symphonyService.sendMessage(symEvent, botResponse[0]);
							}, true);
						}
						userIntent.incrementWrongInputAttempts();
						trySafe(() -> {
							botResponse[0] = unansweredQuestion.getQuestionText();
							symphonyService.sendMessage(symEvent, botResponse[0]);
						}, true);
					}
				}
			}
			persistUserConversationLog(symEvent, botResponse[0]);
		});
	}

	private void checkAndPopulateUserMentions(SymMessage messageSent, List<UserIdMention> mentionedUserIds) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map map = mapper.readValue(messageSent.getEntityData(), Map.class);
			map.forEach((key, value) -> {
				Map internal = (Map) value;
				Iterator iterator = ((Map) ((List) ((Map.Entry) internal.entrySet().iterator().next()).getValue()).get(0)).entrySet().iterator();
				UserIdMention userIdMention = new UserIdMention();
				iterator.next();
				userIdMention.setUserId((Long) ((Map.Entry) iterator.next()).getValue());
				userIdMention.setName(UserUtil.getUser(userIdMention.getUserId()).getDisplayName());
				mentionedUserIds.add(userIdMention);
				System.out.println("User Mentions :: user ID :: " + userIdMention);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean userRoleNotMatchingWorkflow(String userEmailId, String workflowName) {
		UserRoleEnum key = USER_ROLE_MAP.get(userEmailId);
		return !USER_ROLE_MAP.containsKey(userEmailId)
				|| !USER_WORKFLOW_MAP.get(key).equalsIgnoreCase(workflowName);
	}

	private void processUserInput(SymEvent symEvent, ClassifierResponse classifiedResponse, UserInput userInput, String[] botResponse) {
		if (classifiedResponse.getResponseType() == ClassifierResponseType.WORKFLOW) {
			HashMap<String, Object> workflowContext = triggerWorkflow(symEvent, classifiedResponse, userInput);
			botResponse[0] = String.class.cast(workflowContext.get("botResponse"));
		} else {
			trySafe(() -> {
				botResponse[0] = classifiedResponse.getMessage();
				symphonyService.sendMessage(symEvent, botResponse[0]);
			}, true);
		}
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
		UserIntent userIntent = UserUtil.getIntent(symEvent.getInitiator().getId());
		if (userIntent == null) {
			userIntent = new UserIntent();
		}
		userIntent.addInput(new UserInput(symEvent.getPayload().getMessageSent().getMessageText()));
		UserUtil.putIntent(symEvent.getInitiator().getId(), userIntent);
		return userIntent;
	}

	private boolean isUserBot(SymEvent symEvent) {
		return symEvent.getInitiator().getDisplayName().startsWith("bot.user");
	}


	private static enum UserRoleEnum {
		USER, DEV
	}
}
