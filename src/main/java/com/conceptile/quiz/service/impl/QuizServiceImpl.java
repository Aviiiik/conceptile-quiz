package com.conceptile.quiz.service.impl;

import com.conceptile.quiz.entity.QuestionEntity;
import com.conceptile.quiz.entity.ScoreEntity;
import com.conceptile.quiz.repository.QuizRepository;
import com.conceptile.quiz.repository.ScoreRepository;
import com.conceptile.quiz.service.QuizService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

    @Value("${question.count}")
    private int count;

    private final QuizRepository quizRepository;
    private final ScoreRepository scoreRepository; // Add ScoreRepository
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    public QuizServiceImpl(QuizRepository quizRepository,
                           ObjectMapper objectMapper,
                           MongoTemplate mongoTemplate,
                           ScoreRepository scoreRepository) { // Include ScoreRepository
        this.quizRepository = quizRepository;
        this.objectMapper = objectMapper;
        this.mongoTemplate = mongoTemplate;
        this.scoreRepository = scoreRepository; // Initialize ScoreRepository
    }

    @Override
    public void upsertQuestionsFromJson() throws IOException {
        InputStream inputStream = new ClassPathResource("questionAnswer.json").getInputStream();
        List<QuestionEntity> questions = objectMapper.readValue(inputStream, new TypeReference<List<QuestionEntity>>() {});
        log.info("Loaded questions from JSON: {}", questions);

        for (QuestionEntity question : questions) {
            Optional<QuestionEntity> existingQuestion = quizRepository.findById(question.getId());

            if (existingQuestion.isPresent()) {
                QuestionEntity existing = existingQuestion.get();
                existing.setQuestion(question.getQuestion());
                existing.setOptions(question.getOptions());
                existing.setCorrectAnswer(question.getCorrectAnswer());
                quizRepository.save(existing);
            } else {
                quizRepository.save(question);
            }
        }
    }

    @Override
    public List<QuestionEntity> getAllQuestions() {
        return quizRepository.findAll();
    }

    @Override
    public List<QuestionEntity> getRandomQuestions() {
        SampleOperation sampleStage = Aggregation.sample(count);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);

        AggregationResults<QuestionEntity> results = mongoTemplate.aggregate(
                aggregation, "questions", QuestionEntity.class);

        return results.getMappedResults();
    }

    @Override
    public void saveUserScore(String username, int score) {
        ScoreEntity scoreEntity = new ScoreEntity();
        scoreEntity.setUsername(username);
        scoreEntity.setScore(score);
        scoreRepository.save(scoreEntity); // Corrected to use ScoreRepository
    }

    @Override
    public ScoreEntity getHighestScore() {
        return scoreRepository.findTopByOrderByScoreDesc(); // Corrected to use ScoreRepository
    }
}
