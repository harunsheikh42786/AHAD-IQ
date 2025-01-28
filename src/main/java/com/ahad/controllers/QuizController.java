package com.ahad.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ahad.entities.Question;
import com.ahad.entities.Quiz;
import com.ahad.entities.User;
import com.ahad.helper.GenerateQuestion;
import com.ahad.helper.Message;
import com.ahad.services.AnswerService;
import com.ahad.services.QuestionService;
import com.ahad.services.QuizService;
import com.ahad.services.UserService;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final GenerateQuestion generateQuestion;

    public QuizController(GenerateQuestion generateQuestion) {
        this.generateQuestion = generateQuestion;
    }

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

    // @GetMapping("/main")
    // public String main(String quizId, Principal principal) {

    // }

    @PostMapping("/new-quiz")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createQuiz(@ModelAttribute Quiz quiz, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get user from the principal
            User user = this.userService.getUserByEmail(principal.getName());

            // Create quiz and pass the principal
            quiz = quizService.createQuiz(quiz, principal);

            // Fetch updated quizzes list
            response.put("message", new Message("Quiz successfully created.", "success"));
            response.put("quizzes", getQuizzes(principal)); // Send updated quizzes list
        } catch (Exception e) {
            response.put("message", new Message("Error: " + e.getMessage(), "danger"));
            response.put("quizzes", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public String getQuiz(@PathVariable("id") String quizId, Model model, Principal principal) {
        Quiz quiz = quizService.getQuiz(quizId, principal);
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", this.questionService.getQuestionsByQuizId(quizId));
        return "user/quiz-start";
    }

    @GetMapping("/remove-quiz/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeQuiz(@PathVariable("id") String quizId, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isDeleted = quizService.deleteQuiz(quizId, principal);

            response.put("message", new Message("Quiz successfully removed.", "success"));
            response.put("quizzes", getQuizzes(principal));

        } catch (Exception e) {
            response.put("message", new Message("Error: " + e.getMessage(), "danger"));
            response.put("quizzes", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/quizzes")
    @ResponseBody
    public ResponseEntity<List<Quiz>> getQuizzes(Principal principal) {
        List<Quiz> quizzes = this.quizService.findAllQuizByUserId(principal);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/ready-quiz/{id}")
    public ResponseEntity<Boolean> readyQuiz(@PathVariable("id") String quizId, Principal principal) {
        try {
            Quiz quiz = this.quizService.getQuiz(quizId, principal);
            this.generateQuestion.generate(quiz, principal);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

}
