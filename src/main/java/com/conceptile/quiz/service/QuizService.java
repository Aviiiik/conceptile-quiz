package com.conceptile.quiz.service;

import com.conceptile.quiz.entity.QuestionEntity;
import com.conceptile.quiz.entity.ScoreEntity;

import java.io.IOException;
import java.util.List;

public interface QuizService {

    void upsertQuestionsFromJson() throws IOException;

    List<QuestionEntity> getAllQuestions();

    List<QuestionEntity> getRandomQuestions();

    void saveUserScore(String username, int score); // Save the user's score

    ScoreEntity getHighestScore(); // Retrieve the highest score with username
}

