package com.stampbot;

import com.stampbot.knowledge.entity.*;
import com.stampbot.knowledge.repository.MovieRepository;
import com.stampbot.knowledge.repository.RelationshipNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

//@SpringBootApplication
//@EnableNeo4jRepositories
public class KnowledgeApp {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private RelationshipNodeRepository relationshipNodeRepository;

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeApp.class, args);
	}

	@PostConstruct
	@Transactional
	public void save(){
		RelationshipNodeEntity s1 = new RelationshipNodeEntity();
		s1.setName("Again mapping now");
		KnowledgeNodeEntity left = new KnowledgeNodeEntity();
		left.setName("LEFT_!!! ONE");
		s1.setSubject(left);
		KnowledgeNodeEntity right = new KnowledgeNodeEntity();
		right.setName("RIGHT_73272 ONE");
		s1.setObject(right);
		relationshipNodeRepository.save(s1);
		RelationshipNodeEntity againMappingNow = relationshipNodeRepository.findByName("Again mapping now");
		System.out.println(againMappingNow);
	}

}
