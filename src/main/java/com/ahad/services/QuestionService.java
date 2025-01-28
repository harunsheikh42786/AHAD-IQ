package com.ahad.services;

import java.security.Principal;
import java.util.List;

import com.ahad.entities.Question;

public interface QuestionService {

    public Question createQuestion(Question question, Principal principal);

    public Question updateQuestion(Question question, Principal principal);

    public Question getQuestion(String id, Principal principal);

    public void deleteQuestion(String id, Principal principal);

    public List<Question> getQuestionsByQuizId(String quizId);

    public Question getNextQuestion(String quizId, int currentQuestionNumber);
}
