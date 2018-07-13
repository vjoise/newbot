package com.stampbot.workflow.repository;


import com.stampbot.workflow.entity.WorkflowQuestionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowQuestionRepository extends CrudRepository<WorkflowQuestionEntity, Long>{

	@Query("select entity from WorkflowQuestionEntity entity left join entity.workflowEntity where entity.workflowEntity.name = :detectedWorkflow order by entity.id")
	List<WorkflowQuestionEntity> findByWorkflowName(@Param("detectedWorkflow") String detectedWorkflow);

}
