package com.ahad.services;

import java.security.Principal;
import java.util.List;

import com.ahad.entities.Answer;

public interface AnswerService {

    public Answer createAnswer(Answer answer, Principal principal);

    public Answer updateAnswer(Answer answer, Principal principal);

    public Answer getAnswer(String id);

    public boolean deleteAnswer(String id);

    public List<Answer> findAllByQuestionId(String questionId);

}
