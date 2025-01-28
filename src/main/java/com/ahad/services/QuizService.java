package com.ahad.services;

import java.security.Principal;
import java.util.List;

import com.ahad.entities.Quiz;

public interface QuizService {
    public Quiz createQuiz(Quiz quiz, Principal principal);

    public Quiz updateQuiz(Quiz quiz, Principal principal);

    public Quiz getQuiz(String id, Principal principal);

    public boolean deleteQuiz(String id, Principal principal);

    public List<Quiz> findAllQuizByUserId(Principal principal);
}
