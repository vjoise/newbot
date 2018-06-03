package com.stampbot.repository;

import com.stampbot.entity.UserWorkflowLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkflowRepository extends CrudRepository<UserWorkflowLog, Long>{

	@Query("select count(1) = 0 from UserWorkflowLog log where log.userId = :userId and log.conversationId = :conversationId and log.passed = false")
	boolean isEmpty(@Param("userId") String userId, @Param("conversationId") String conversationId);

	@Query("select log from UserWorkflowLog log where log.passed = false and log.conversationId = :conversationId")
	UserWorkflowLog getUnansweredQuestion(@Param("conversationId") String conversationId);
}
