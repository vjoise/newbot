package com.stampbot.service.reminder;

import com.stampbot.config.StampBotConfig;
import com.stampbot.entity.ActionItemEntity;
import com.stampbot.service.action.ActionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.StreamsException;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.symphony.clients.StreamsClient;
import org.symphonyoss.symphony.clients.model.SymMessage;
import org.symphonyoss.symphony.pod.model.UserIdList;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* If there is no response from the user, a reminder is sent out to that user.*/
@Component
public class ActionItemReminderService {

	@Autowired
	private ActionItemService actionItemService;

	@Autowired
	private StampBotConfig config;

	@Autowired
	private SymphonyClient symphonyClient;

	private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

	public void scheduleReminder(List<ActionItemEntity> entities) {
		entities.forEach(entity -> {
			executorService.schedule(() -> {
				boolean actionPending = actionItemService.isActionPending(entity.getId());
				if(actionPending){
					Chat chat = new Chat();
					SymMessage aMessage = new SymMessage();
					chat.setLastMessage(aMessage);
					StreamsClient streamsClient = symphonyClient.getStreamsClient();
					UserIdList userIdList = new UserIdList();
					userIdList.add(entity.getUserId());
					try {
						chat.setStream(streamsClient.getStream(userIdList));
						symphonyClient.getMessageService().sendMessage(chat, aMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, Long.parseLong(config.getTaskReminderInterval()), TimeUnit.SECONDS);
		});

	}

}
