package com.stampbot.repository;


import com.stampbot.entity.WorkflowQuestionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowQuestionRepository extends CrudRepository<WorkflowQuestionEntity, Long>{

	@Query("select entity from WorkflowQuestionEntity entity left join entity.workflowEntity where entity.workflowEntity.name = :detectedWorkflow")
	WorkflowQuestionEntity findByWorkflowName(@Param("detectedWorkflow") String detectedWorkflow);
}
