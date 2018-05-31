package com.stampbot.service.symphony;

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

@Slf4j
@Component
public class DataFeedProcessor {

    @Autowired
    private SymphonyClient symphonyClient;

    private DataFeedClient dataFeedClient;

    private SymDatafeed dataFeed;

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
            log.info("Message from user :: " + symEvent.getPayload().getMessageSent().getMessageText());
            try {
                Chat chat = new Chat();
                SymMessage aMessage = new SymMessage();
                aMessage.setMessageText("Hello master, I'm alive again....");
                chat.setLastMessage(aMessage);
                StreamsClient streamsClient = symphonyClient.getStreamsClient();
                chat.setStream(streamsClient.getStream(symEvent.getInitiator()));
                symphonyClient.getMessageService().sendMessage(chat, aMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
