package com.stampbot.knowledge.repository;

import com.stampbot.knowledge.entity.KnowledgeNodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

public interface KnowledgeRepository extends Neo4jRepository<KnowledgeNodeEntity, Long> {
}
