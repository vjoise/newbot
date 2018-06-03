package com.stampbot.service.symphony;

import com.stampbot.domain.UserInput;
import com.stampbot.domain.UserInputWord;
import com.stampbot.service.nlp.MessageParser;
import com.stampbot.service.task.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.events.SymEvent;
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

            UserInput userInput = messageParser.parseInputMessage(messageText);
            if (!userInput.isNegativeSentiment()) {
                //valid statement either neutral or positive, find appropriate answer here.
                List<String> ids = userInput.getWords().stream()./*filter(word -> word.getEntity().equalsIgnoreCase("TASK")).*/map(UserInputWord::getWord).collect(Collectors.toList());
                trySafe(() -> {
                    List<String> strings = taskService.validateIds(ids);
                    log.info("Valid ones :: " + strings);
                });
            }

            if (toCreateSubTask(userInput)) {
                String parentJiraToStartTesting = startTestingForJira(userInput);
                try {
                    String newJira = taskService.createSubTask(parentJiraToStartTesting);
                    Chat chat = new Chat();
                    SymMessage aMessage = new SymMessage();
                    aMessage.setMessageText("Testing Sub-Task " + newJira + " created for " + parentJiraToStartTesting + ".");
                    chat.setLastMessage(aMessage);
                    StreamsClient streamsClient = symphonyClient.getStreamsClient();
                    chat.setStream(streamsClient.getStream(symEvent.getInitiator()));
                    symphonyClient.getMessageService().sendMessage(chat, aMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Chat chat = new Chat();
                    SymMessage aMessage = new SymMessage();
                    aMessage.setMessageText("Echo Response :: " + messageText);
                    chat.setLastMessage(aMessage);
                    StreamsClient streamsClient = symphonyClient.getStreamsClient();
                    chat.setStream(streamsClient.getStream(symEvent.getInitiator()));
                    symphonyClient.getMessageService().sendMessage(chat, aMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean toCreateSubTask(UserInput userInput) {
        String parentJiraToStartTesting = startTestingForJira(userInput);

        return !parentJiraToStartTesting.equals("");
    }

    private String startTestingForJira(UserInput userInput) {
        if (userInput.getInputSentence().contains("test") && userInput.getInputSentence().contains("-")) {
            Optional<UserInputWord> jiraUserInputWord = userInput.getWords()
                    .stream()
                    .filter(userInputWord -> userInputWord.getWord().matches("((([a-zA-Z]{1,10})-)*[a-zA-Z]+-\\d+)"))
                    .findFirst();
            if (jiraUserInputWord.isPresent()) {
                return jiraUserInputWord.get().getWord();
            } else {
                return "";
            }
        }
        return "";
    }
}
