package com.stampbot.knowledge.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="REL")
@Data
public class RelationshipNodeEntity {

	@GraphId
	Long id;

	private String name;

	@StartNode
	private KnowledgeNodeEntity subject;

	@EndNode
	private KnowledgeNodeEntity object;

}
