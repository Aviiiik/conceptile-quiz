package com.conceptile.quiz.controller;

import com.conceptile.quiz.entity.QuestionEntity;
import com.conceptile.quiz.entity.ScoreEntity;
import com.conceptile.quiz.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/conceptile/quiz/v1")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/upsert")
    public ResponseEntity<String> upsertQuestions() {
        try {
            quizService.upsertQuestionsFromJson();
            log.info("Questions upserted successfully!");
            return ResponseEntity.ok("Questions upserted successfully!");
        } catch (Exception e) {
            log.error("Failed to upsert questions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the JSON file.");
        }
    }

    @GetMapping("/questionAnswers")
    public ResponseEntity<List<QuestionEntity>> getAllQuestions() {
        try {
            List<QuestionEntity> questions = quizService.getAllQuestions();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            log.error("Error fetching questions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/ui")
    public String getQuizPage(Model model) {
        List<QuestionEntity> questions = quizService.getRandomQuestions();
        model.addAttribute("questions", questions);
        return "quiz"; // Refers to the Thymeleaf template "quiz.html"
    }

    @PostMapping("/submitScore")
    public ResponseEntity<String> submitScore(@RequestParam String username, @RequestParam int score) {
        try {
            quizService.saveUserScore(username, score);
            return ResponseEntity.ok("Score saved successfully!");
        } catch (Exception e) {
            log.error("Error saving score: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save score.");
        }
    }

    @GetMapping("/highestScore")
    public ResponseEntity<ScoreEntity> getHighestScore() {
        try {
            return ResponseEntity.ok(quizService.getHighestScore());
        } catch (Exception e) {
            log.error("Error fetching highest score: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
