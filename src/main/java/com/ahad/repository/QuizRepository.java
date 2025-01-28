package com.ahad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, String> {

    public List<Quiz> findAllQuizByUserId(String userId);

}
