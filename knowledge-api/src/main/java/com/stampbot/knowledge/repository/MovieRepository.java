package com.stampbot.knowledge.repository;

import com.stampbot.knowledge.entity.Movie;
import com.stampbot.knowledge.entity.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MovieRepository extends Neo4jRepository<Movie, Long> {

	// derived finder
	Movie findByTitle(String title);

	@Query("MATCH (movie:Movie)-[:HAS_GENRE]->(genre)<-[:HAS_GENRE]-(similar) WHERE id(movie) = {0} RETURN similar")
	List<Movie> findSimilarMovies(Movie movie);
}