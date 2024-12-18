package com.conceptile.quiz.repository;

import com.conceptile.quiz.entity.ScoreEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends MongoRepository<ScoreEntity, String> {

    // Find the highest score
    ScoreEntity findTopByOrderByScoreDesc();
}
