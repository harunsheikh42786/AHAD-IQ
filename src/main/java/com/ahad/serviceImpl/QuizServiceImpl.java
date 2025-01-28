package com.ahad.serviceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahad.entities.Quiz;
import com.ahad.entities.User;
import com.ahad.repository.QuizRepository;
import com.ahad.services.QuizService;
import com.ahad.services.UserService;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Quiz createQuiz(Quiz quiz, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());

        quiz.setId(UUID.randomUUID().toString());
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUser(user);
        quiz.setCurrentQuestionNumber(-1);
        return this.quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Quiz quiz, Principal principal) {
        return this.quizRepository.save(quiz);
    }

    @Override
    public Quiz getQuiz(String id, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        return user.getQuizzes().stream().filter(q -> q.getId().equals(id)).findFirst().get();
    }

    @Override
    public boolean deleteQuiz(String id, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        Quiz quiz = user.getQuizzes().stream().filter(q -> q.getId().equals(id)).findFirst().get();
        user.getQuizzes().remove(quiz);
        this.quizRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Quiz> findAllQuizByUserId(Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        return this.quizRepository.findAllQuizByUserId(user.getId());
    }

}
