package com.ahad.serviceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahad.entities.Answer;
import com.ahad.repository.AnswerRepository;
import com.ahad.services.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Answer createAnswer(Answer answer, Principal principal) {
        return this.answerRepository.save(answer);
    }

    @Override
    public Answer updateAnswer(Answer answer, Principal principal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAnswer'");
    }

    @Override
    public Answer getAnswer(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAnswer'");
    }

    @Override
    public boolean deleteAnswer(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAnswer'");
    }

    @Override
    public List<Answer> findAllByQuestionId(String questionId) {
        return this.answerRepository.findAllAnswerByQuestionId(questionId);
    }

}
