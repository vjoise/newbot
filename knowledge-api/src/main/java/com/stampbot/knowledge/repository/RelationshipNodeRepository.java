package com.stampbot.knowledge.repository;

import com.stampbot.knowledge.entity.RelationshipNodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RelationshipNodeRepository extends Neo4jRepository<RelationshipNodeEntity, Long>{

	RelationshipNodeEntity findByName(String name);

}
