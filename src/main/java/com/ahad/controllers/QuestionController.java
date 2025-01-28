package com.ahad.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.stringtemplate.v4.compiler.STParser.ifstat_return;

import com.ahad.entities.Question;
import com.ahad.entities.Quiz;
import com.ahad.entities.User;
import com.ahad.helper.GenerateQuestion;
import com.ahad.helper.Message;
import com.ahad.services.AnswerService;
import com.ahad.services.QuestionService;
import com.ahad.services.QuizService;
import com.ahad.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private GenerateQuestion generateQuestion;
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;

    @ModelAttribute
    public void modelHandler(Model model, Principal principal) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            // User is authenticated
            model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        } else {
            // User is not authenticated
            model.addAttribute("user", new User());
        }
        model.addAttribute("message", new Message());
    }

    @GetMapping("/get-new/{id}")
    public ResponseEntity<?> newQuestion(@PathVariable("id") String quizId, Model model, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        Quiz quiz = user.getQuizzes().stream()
                .filter(q -> q.getId().equals(quizId))
                .findFirst()
                .orElse(null);

        quiz = this.generateQuestion.generate(quiz, principal); // Generate question
        Question question = this.questionService.getNextQuestion(quizId, quiz.getCurrentQuestionNumber());
        model.addAttribute("quiz", quiz); // Add question to model
        model.addAttribute("question", question); // Add question to model
        return ResponseEntity.ok(question); // Return question as JSON
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@PathVariable("id") String quizId) {
        return ResponseEntity.ok(this.questionService.getQuestionsByQuizId(quizId));
    }

    @GetMapping("/{id}/{number}")
    public ResponseEntity<Question> getQuestion(@PathVariable("id") String quizId,
            @PathVariable("number") int questionNumber) {
        return ResponseEntity.ok(this.questionService.getNextQuestion(quizId, questionNumber));
    }

    @GetMapping("/submit/{id}/{option}/{status}")
    public ResponseEntity<String> submitAnswer(@PathVariable("id") String questionId,
            @PathVariable("option") char submitOption, @PathVariable("status") boolean answerStatus,
            Principal principal) {
        try {

            // Question fetch karo
            Question question = this.questionService.getQuestion(questionId, principal);
            Quiz quiz = this.quizService.getQuiz(question.getQuiz().getId(), principal);

            if (answerStatus) {
                // Constant for maximum score
                final double MAX_SCORE = 100.00;
                double score = quiz.getTotalScore() + (MAX_SCORE / quiz.getNumberOfQuestions());

                // Cap the score at 100
                double cappedScore = Math.min(score, MAX_SCORE);

                // Round to 2 decimal places
                cappedScore = Math.round(cappedScore * 100.0) / 100.0;

                quiz.setTotalScore(cappedScore);

                quiz.setCurrentQuestionNumber(quiz.getCurrentQuestionNumber() + 1);
            }

            question.setSubmitOption(submitOption);
            question.setSubmitAnswer(true);

            // Question update karo
            this.questionService.updateQuestion(question, principal);
            this.quizService.updateQuiz(quiz, principal);

            // Response bhejein
            return ResponseEntity.ok("Answer submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to submit answer");
        }
    }

}
