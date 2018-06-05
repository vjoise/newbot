package com.stampbot.service.reminder;

import com.stampbot.config.StampBotConfig;
import com.stampbot.entity.ActionItemEntity;
import com.stampbot.entity.UserWorkflowLogEntity;
import com.stampbot.entity.UserWorkflowMasterEntity;
import com.stampbot.service.action.ActionItemService;
import com.stampbot.service.workflow.UserWorkflowStore;
import jersey.repackaged.com.google.common.collect.Lists;
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

import static com.stampbot.common.Utils.trySafe;

/* If there is no response from the user, a reminder is sent out to that user.*/
@Component
public class ActionItemReminderService {

	@Autowired
	private ActionItemService actionItemService;

	@Autowired
	private StampBotConfig config;

	@Autowired
	private SymphonyClient symphonyClient;

	@Autowired
	private UserWorkflowStore workflowStore;

	private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

	public void scheduleReminder(List<ActionItemEntity> entities) {
		entities.forEach(entity -> {
			executorService.schedule(() -> {
				boolean actionPending = actionItemService.isActionPending(entity.getId());
				if (actionPending) {
					Chat chat = new Chat();
					SymMessage aMessage = new SymMessage();
					aMessage.setMessageText("KAKI Reminder :: Please start this action item :: " + entity.getResource());
					/*UserWorkflowLogEntity workflowLogEntity = new UserWorkflowLogEntity();
					workflowLogEntity.setStatus("ACTIVE");
					workflowLogEntity.setWorkflowId(entity.getWorkflowId());
					workflowLogEntity.setUserId(entity.getUserId());
					workflowLogEntity.setConversationId(entity.getConversationId());
					UserWorkflowMasterEntity masterWorkflowEntityById = workflowStore.findMasterWorkflowEntityById(entity.getMasterWorkflowId());
					masterWorkflowEntityById.getUserWorkflowLogEntities().add(workflowLogEntity);
					masterWorkflowEntityById.setStatus("ACTIVE");
					workflowStore.save(masterWorkflowEntityById);*/
					chat.setLastMessage(aMessage);
					StreamsClient streamsClient = symphonyClient.getStreamsClient();
					UserIdList userIdList = new UserIdList();
					userIdList.add(entity.getUserId());
					trySafe(() -> {
						chat.setStream(streamsClient.getStream(userIdList));
						symphonyClient.getMessageService().sendMessage(chat, aMessage);
					}, true);
				}
			}, Long.parseLong(config.getTaskReminderInterval()), TimeUnit.SECONDS);
		});

	}

}
