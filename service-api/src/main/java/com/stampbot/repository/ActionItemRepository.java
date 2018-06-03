package com.stampbot.repository;

import com.stampbot.entity.ActionItemEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionItemRepository extends CrudRepository<ActionItemEntity, Long>{

	@Query("select count(*) > 0 from ActionItemEntity entity where entity.updatedDate - 1 > CURRENT_DATE() and entity.status = 'OPEN'")
	boolean isActionPending();
}
