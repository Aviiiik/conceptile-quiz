package com.conceptile.quiz.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "scores")
public class ScoreEntity {

    @Id
    private String id;
    private String username;
    private int score;
}
