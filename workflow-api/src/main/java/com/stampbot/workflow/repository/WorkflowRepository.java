package com.stampbot.workflow.repository;

import com.stampbot.workflow.entity.WorkflowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends JpaRepository<WorkflowEntity, Long> {

	WorkflowEntity findByName(String detectedWorkflow);

}
