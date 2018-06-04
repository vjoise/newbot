package com.stampbot.repository;

import com.stampbot.entity.WorkflowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends CrudRepository<WorkflowEntity, Long>{

	WorkflowEntity findByName(String detectedWorkflow);

}
