package com.stampbot.workflow.service.reminder;

import com.stampbot.workflow.entity.ActionItemEntity;
import com.stampbot.workflow.repository.ActionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActionItemService {

	@Autowired
	private ActionItemRepository repository;

	//@Autowired
	private ActionItemReminderService reminderService;

	public void createActionItems(List<ActionItemEntity> entityList){
		repository.save(entityList);
		//reminderService.scheduleReminder(entityList);
	}

	public boolean isActionPending(Long id) {
		return repository.isActionPending(id);
	}
}
