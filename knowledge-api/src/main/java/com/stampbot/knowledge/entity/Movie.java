package com.stampbot.knowledge.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "ANOTHER_MOVIE")
@Data
public class Movie {

	@GraphId
	Long id;

	String title;

	Person director;

	@Relationship(type="ACTS_IN", direction = Relationship.INCOMING)
	Set<Person> actors;

}
