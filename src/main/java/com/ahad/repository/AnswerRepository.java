package com.ahad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, String> {
    public List<Answer> findAllAnswerByQuestionId(String qustionId);
}
