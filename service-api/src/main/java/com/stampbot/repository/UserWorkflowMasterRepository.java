package com.stampbot.repository;

import com.stampbot.entity.UserWorkflowMasterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkflowMasterRepository extends CrudRepository<UserWorkflowMasterEntity, Long> {

	@Query("select count(1) = 0 from UserWorkflowMasterEntity entity where entity.userId = :userId and entity.conversationId = :conversationId and status = 'ACTIVE'")
	boolean isEmpty(@Param("userId") Long userId, @Param("conversationId") String conversationId);

	@Query("select entity from UserWorkflowMasterEntity entity where entity.userId = :userId and entity.conversationId = :conversationId and status = 'ACTIVE'")
	UserWorkflowMasterEntity findByUserIdAndConversationId(@Param("userId") Long userId, @Param("conversationId") String conversationId);
}
