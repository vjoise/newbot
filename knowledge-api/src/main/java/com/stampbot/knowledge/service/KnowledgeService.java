package com.stampbot.knowledge.service;

import com.stampbot.knowledge.entity.KnowledgeNodeEntity;
import com.stampbot.knowledge.entity.RelationshipNodeEntity;
import com.stampbot.knowledge.repository.RelationshipNodeRepository;
import com.stampbot.lang.domain.InputSentence;
import com.stampbot.lang.domain.SentenceRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KnowledgeService {

	@Autowired
	private RelationshipNodeRepository relationshipNodeRepository;

	@Transactional
	public KnowledgeNodeEntity store(InputSentence sentence) {
		RelationshipNodeEntity relationshipNodeEntity = new RelationshipNodeEntity();
		SentenceRelation relation = sentence.getRelation();
		relationshipNodeEntity.setName(relation.getRelation());
		KnowledgeNodeEntity subject = new KnowledgeNodeEntity();
		subject.setName(relation.getSubject());
		relationshipNodeEntity.setSubject(subject);
		KnowledgeNodeEntity object = new KnowledgeNodeEntity();
		object.setName(relation.getObject());
		relationshipNodeEntity.setObject(object);
		relationshipNodeRepository.save(relationshipNodeEntity);
		return subject;
	}

	public KnowledgeNodeEntity find(InputSentence sentence) {
		RelationshipNodeEntity byName = relationshipNodeRepository.findByName(sentence.getRelation().getRelation());
		return byName!=null ? byName.getObject() : store(sentence);
	}

}
