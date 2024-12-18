package com.conceptile.quiz.repository;

import com.conceptile.quiz.entity.QuestionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends MongoRepository<QuestionEntity, String> {

}
