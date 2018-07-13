package com.stampbot.knowledge.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Topic {

	@GraphId
	private String subject;

	private String object;

	private String relation;
}
