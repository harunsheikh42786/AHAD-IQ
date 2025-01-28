package com.ahad.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.ahad.entities.enums.QuizType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ahad.entities.enums.QuizLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Quiz {

    @Id
    private String id; // Unique identifier for the quiz
    private String name; // Name of the quiz
    private LocalDateTime createdAt; // Timestamp when the quiz was created
    private double totalScore; // Total score obtainable in the quiz
    private int numberOfQuestions; // Total number of questions in the quiz
    private int currentQuestionNumber; // Total number of questions in the quiz
    @Enumerated(EnumType.STRING)
    private QuizLevel quizLevel; // Total score obtainable in the quiz
    @Enumerated(EnumType.STRING)
    private QuizType quizType; // Total score obtainable in the quiz
    private boolean isReady; // Total score obtainable in the quiz
    @JsonIgnore
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions; // List of questions in the quiz
    @JsonIgnore
    @ManyToOne
    private User user; // User who created the quiz
}
