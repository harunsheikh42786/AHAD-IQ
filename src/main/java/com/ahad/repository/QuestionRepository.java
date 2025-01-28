package com.ahad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, String> {

    public List<Question> findQuestionByQuizId(String quizId);

    public List<Question> findAllQuestionByQuizId(String quizId);

}
