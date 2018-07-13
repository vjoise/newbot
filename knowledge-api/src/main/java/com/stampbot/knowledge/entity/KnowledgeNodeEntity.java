package com.stampbot.knowledge.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@NodeEntity
@Data
public class KnowledgeNodeEntity {

	@GraphId
	private Long id;

	private String name;

	private boolean answerFound;

	private String answer;

}
