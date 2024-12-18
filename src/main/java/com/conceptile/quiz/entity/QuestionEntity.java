package com.conceptile.quiz.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "questions")
@TypeAlias("questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionEntity {

    @Id
    @Field("_id") // Map to MongoDB _id field
    private String id;
    private String question;
    private List<String> options;
    private String correctAnswer;
}