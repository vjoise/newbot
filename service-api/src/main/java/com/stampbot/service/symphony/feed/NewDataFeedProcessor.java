package com.stampbot.service.symphony.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stampbot.config.StampBotConfig;
import com.stampbot.domain.UserIdMention;
import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserIntent;
import com.stampbot.entity.UserBotConversationLog;
import com.stampbot.knowledge.entity.KnowledgeNodeEntity;
import com.stampbot.knowledge.service.KnowledgeService;
import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.domain.SentenceClassification;
import com.stampbot.lang.domain.SentenceTypeEnum;
import com.stampbot.lang.domain.WordPOSMapping;
import com.stampbot.lang.service.classifier.GenericSentenceClassifier;
import com.stampbot.lang.service.classifier.WorkflowSentenceClassifier;
import com.stampbot.lang.service.conversation.responder.BotResponse;
import com.stampbot.lang.service.conversation.responder.BotResponseProvider;
import com.stampbot.repository.UserBotConversationRepository;
import com.stampbot.service.nlp.MessageParser;
import com.stampbot.service.nlp.classifier.ClassifierResponse;
import com.stampbot.service.nlp.classifier.ClassifierResponseType;
import com.stampbot.service.nlp.classifier.UserInputClassifier;
import com.stampbot.service.symphony.service.SymphonyService;
import com.stampbot.util.UserUtil;
import com.stampbot.workflow.entity.UserWorkflowLogEntity;
import com.stampbot.workflow.entity.UserWorkflowMasterEntity;
import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import com.stampbot.workflow.model.WorkflowContext;
import com.stampbot.workflow.repository.UserWorkflowStore;
import com.stampbot.workflow.repository.WorkflowQuestionRepository;
import com.stampbot.workflow.service.WorkflowService;
import com.stampbot.workflow.service.WorkflowServiceHelper;
import com.stampbot.workflow.service.type.WorkflowQuestionHandler;
import lombok.extern.slf4j.Slf4j;
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
import org.symphonyoss.symphony.clients.model.SymUser;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.stampbot.common.Utils.trySafe;

@Slf4j
@Component
public class NewDataFeedProcessor {

	@Autowired
	private SymphonyClient symphonyClient;

	private DataFeedClient dataFeedClient;

	private SymDatafeed dataFeed;

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

	@Autowired
	private WorkflowSentenceClassifier workflowSentenceClassifier;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private BotResponseProvider responder;

	@Autowired
	private MessageParser messageParser;

	@Autowired
	private GenericSentenceClassifier genericSentenceClassifier;

	@Autowired
	private WorkflowServiceHelper workflowServiceHelper;

	private static final Map<String, UserRoleEnum> USER_ROLE_MAP = Maps.newHashMap();
	private static final Map<UserRoleEnum, String> USER_WORKFLOW_MAP = Maps.newHashMap();

	@PostConstruct
	public void init() throws Exception {
		log.info("Initializing the DataFeedClient");
		dataFeedClient = symphonyClient.getDataFeedClient();
		dataFeed = dataFeedClient.createDatafeed(ApiVersion.V4);
		log.info("DataFeedClient ID :: " + dataFeedClient);
		log.info("Data Feed ID :: " + dataFeed.getId());
		USER_ROLE_MAP.put("jingyu.li@credit-suisse.com", UserRoleEnum.USER);
		USER_ROLE_MAP.put("ravi.adhikarla@credit-suisse.com", UserRoleEnum.USER);
		USER_ROLE_MAP.put("venkatesh.joisekrishnamurthy@credit-suisse.com", UserRoleEnum.DEV);
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
			InputSentence inputSentence = new InputSentence(messageSent.getMessageText());
			persistUserConversationLog(symEvent, null);
			UserIntent userIntent = extractUserIntent(symEvent);
			UserInput userInput = messageParser.parseInputMessage(userIntent);
			inputSentence.setWords(userInput.getWords()
					.stream()
					.map(word -> new WordPOSMapping(word.getWord(), word.getPos(), word.getEntity()))
					.collect(Collectors.toList()));
			inputSentence.setRelation(userInput.getSentenceRelation());
			trySafe(() -> {
				/*Check if the user is part of a workflow or not here??*/
				boolean noPendingWorkflowTasks = userWorkflowStore.isEmpty(symEvent.getInitiator().getId(), messageSent.getStreamId());
				String botData = "";
				if (noPendingWorkflowTasks) {
					botData = classifyAndProcess(symEvent, inputSentence);
				} else {
					final Class<?>[] handlerClass = {null};
					UserWorkflowLogEntity nextUnansweredQuestion = userWorkflowStore.findNextUnansweredQuestion(messageSent.getStreamId());
					WorkflowQuestionEntity unansweredQuestion = questionRepository.findOne(nextUnansweredQuestion.getQuestionId());
					userInput.setQuestionEntity(unansweredQuestion);
					log.info("Found the workflow associated to this chat :: " + unansweredQuestion.getWorkflowEntity().getName() + " and action handler :: " +
							unansweredQuestion.getActionHandler());
					stringToClass(unansweredQuestion.getActionHandler(), handlerClass);
					WorkflowContext workflowContext = new WorkflowContext();
					workflowContext.setConversationId(messageSent.getStreamId());
					workflowContext.setUserId(symEvent.getInitiator().getId());
					workflowContext.setQuestionEntity(unansweredQuestion);
					if (handlerClass[0] != null) {
						WorkflowQuestionHandler questionHandler = WorkflowQuestionHandler.class.cast(context.getBean(handlerClass[0]));
						questionHandler.handle(workflowContext);
						if (workflowContext.isSuccessFullyHandled()) {
							workflowServiceHelper.logWorkflowRecords(workflowContext);
						}
					} else {
						//respond to user with the current question configured.
						WorkflowQuestionEntity finalUnansweredQuestion = unansweredQuestion;
						trySafe(() -> symphonyService.sendMessage(symEvent, finalUnansweredQuestion.getQuestionText()), false);
					}

				}
				persistUserConversationLog(symEvent, botData);
			}, true);
		});
	}

	public void stringToClass(String stringName, Class<?>[] actionHandlerClass) {
		trySafe(() -> {
			actionHandlerClass[0] = Class.forName(stringName);
		}, false);
	}

	private String classifyAndProcess(SymEvent symEvent, InputSentence inputSentence) throws Exception {
		final SentenceClassification[] classification = {null};
		final SentenceTypeEnum[] questionClassified = {null};
		CompletionService<String> completionService = new ExecutorCompletionService<>(Executors.newCachedThreadPool());
		completionService.submit(() -> {
			trySafe(() -> classification[0] = workflowSentenceClassifier.classify(inputSentence), false);
			return "DONE";
		});
		completionService.submit(() -> {
			questionClassified[0] = genericSentenceClassifier.classify(inputSentence.getSentence());
			return "DONE";
		});
		for (int i = 0; i < 2; i++)
			completionService.take().get();
		Map<String, Object> workflowContext = Maps.newHashMap();
		String botResponse = "";
		switch (classification[0].getSentenceClassificationTypeEnum()) {
			case WORKFLOW:
				workflowContext.put("WORKFLOW_ID", classification[0].getWorkflow());
				workflowContext.put("USER_ID", symEvent.getInitiator().getId());
				workflowContext.put("CONVERSATION_ID", symEvent.getPayload().getMessageSent().getStreamId());
				workflowService.process(workflowContext);
				break;
			case NONE:
				trySafe(() -> {
					BotResponse userResponse = responder.getUserResponse(inputSentence);
					if (userResponse.getClassified()) {
						symphonyService.sendMessage(symEvent, userResponse.getData());
					} else {
						switch (questionClassified[0]) {
							case QUESTION:
								KnowledgeNodeEntity nodeEntity = knowledgeService.find(inputSentence);
								if (nodeEntity != null && nodeEntity.isAnswerFound()) {
									symphonyService.sendMessage(symEvent, nodeEntity.getAnswer());
								} else {
									symphonyService.sendMessage(symEvent, "I am sorry, I have no answer for that one.");
								}
								break;
							case STATEMENT:
								knowledgeService.store(inputSentence);
								symphonyService.sendMessage(symEvent, "Ok, I didn't know about it. I will remember that!");
								break;
							case NONE:
								symphonyService.sendMessage(symEvent, "Sorry, didn't get that one!");
								log.info("This is neither a question nor a statement!");
						}
					}
				}, true);
		}
		return botResponse;
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
				trySafe(() -> {
					SymUser userFromId = symphonyClient.getUsersClient().getUserFromId(userIdMention.getUserId());
					userIdMention.setName(userFromId.getDisplayName());
					UserUtil.addUser(userFromId);
				}, false);
				//userIdMention.setName(UserUtil.getUser(userIdMention.getUserId()).getDisplayName());
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
		Object workflowEnded = workflowContext.get("workflowEnded");
		if (workflowEnded != null && (boolean) workflowEnded) {
			UserWorkflowMasterEntity masterWorkflowEntity = userWorkflowStore.findMasterWorkflowEntity(userInput.getUserId(), userInput.getConversationId());
			masterWorkflowEntity.getUserWorkflowLogEntities().forEach(entity -> entity.setStatus("INACTIVE"));
			masterWorkflowEntity.setStatus("INACTIVE");
			userWorkflowStore.save(masterWorkflowEntity);
		}

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
