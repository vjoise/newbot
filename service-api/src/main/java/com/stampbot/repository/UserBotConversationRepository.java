package com.stampbot.repository;

import com.stampbot.entity.UserBotConversationLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBotConversationRepository extends CrudRepository<UserBotConversationLog, Long>{

	UserBotConversationLog findByConversationIdAndUserId(String conversationId, Long userId);
}
