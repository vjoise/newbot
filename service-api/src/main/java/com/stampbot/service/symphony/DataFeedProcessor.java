package com.stampbot.service.symphony;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.model.IssueResponse;
import com.stampbot.service.nlp.MessageParser;
import com.stampbot.service.nlp.classifier.UserInputClassifier;
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
    private TaskService taskService;

    @Autowired
    private UserInputClassifier userInputClassifier;

    @Autowired
    private WorkflowService workflowService;

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
            if (symEvent.getInitiator().getDisplayName().startsWith("bot.user")) {
                return;
            }
            String messageText = symEvent.getPayload().getMessageSent().getMessageText();
            log.info("Message from user  :: " + messageText);
            ClassifierResponse classifiedResponse = userInputClassifier.classify(messageText);
            if (classifiedResponse.getResponseType() == ClassifierResponseType.WORKFLOW) {
                /*This seems to be hitting the workflowEntity, so we need to build the questionnaire for this workflowEntity.*/
                workflowService.process(classifiedResponse.getMessage());
            }
            UserInput userInput = messageParser.parseInputMessage(messageText);
            if (!userInput.isNegativeSentiment()) {
                //valid statement either neutral or positive, find appropriate answer here.
                List<String> ids = userInput.getWords().stream()./*filter(word -> word.getEntity().equalsIgnoreCase("TASK")).*/map(UserInputWord::getWord).collect(Collectors.toList());
                trySafe(() -> {
                    List<String> strings = taskService.validateIds(ids);
                    log.info("Valid ones :: " + strings);
                }, true);
            }

            createSubTask(symEvent, userInput);

			trySafe(() -> {
                replyMessage(symEvent, "Echo Response :: " + messageText);
            }, true);

        });
    }

    private void createSubTask(SymEvent symEvent, UserInput userInput) {
        if (toCreateSubTask(userInput)) {
            String parentJiraKey = startTestingForJira(userInput);
            try {
                IssueResponse issueResponse = taskService.createSubTask(parentJiraKey);
                if (issueResponse != null && issueResponse.getKey() != null) {
                    replyMessage(symEvent, "Testing Sub-Task " + issueResponse.getKey() + " created for " + parentJiraKey + ".");
                } else {
                    throw new Exception("Invalid Response");
                }
            } catch (Exception e) {
                e.printStackTrace();
				trySafe(() -> {
                    replyMessage(symEvent, "Sub-Task could not be created for " + parentJiraKey + ": " + e.getMessage());
                }, true);
            }
        }
    }

    private void replyMessage(SymEvent symEvent, String messageText) throws StreamsException, MessagesException {
        Chat chat = new Chat();
        SymMessage aMessage = new SymMessage();
        aMessage.setMessageText(messageText);
        chat.setLastMessage(aMessage);
        StreamsClient streamsClient = symphonyClient.getStreamsClient();
        chat.setStream(streamsClient.getStream(symEvent.getInitiator()));
        symphonyClient.getMessageService().sendMessage(chat, aMessage);
    }

    private boolean toCreateSubTask(UserInput userInput) {
        return !startTestingForJira(userInput).equals("");
    }

    private String startTestingForJira(UserInput userInput) {
        if (userInput.getInputSentence().contains("test") && userInput.getInputSentence().contains("-")) {
            Optional<UserInputWord> jiraKey = userInput.getWords()
                    .stream()
                    .filter(userInputWord -> userInputWord.getWord().matches("((([a-zA-Z]{1,10})-)*[a-zA-Z]+-\\d+)"))
                    .findFirst();
            if (jiraKey.isPresent()) {
                return jiraKey.get().getWord();
            } else {
                return "";
            }
        }
        return "";
    }
}
