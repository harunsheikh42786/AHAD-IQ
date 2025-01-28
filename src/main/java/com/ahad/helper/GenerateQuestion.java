package com.ahad.helper;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ahad.entities.Answer;
import com.ahad.entities.Question;
import com.ahad.entities.Quiz;
import com.ahad.services.AnswerService;
import com.ahad.services.QuestionService;
import com.ahad.services.QuizService;

@Configuration
public class GenerateQuestion {

        @Autowired
        private QuizService quizService;
        @Autowired
        private QuestionService questionService;
        @Autowired
        private AnswerService answerService;

        private OllamaChatModel chatModel;

        GenerateQuestion(OllamaChatModel chatModel) {
                this.chatModel = chatModel;
        }

        public Quiz generate(Quiz quiz, Principal principal) {

                for (int i = 0; i < quiz.getNumberOfQuestions(); i++) {
                        String quizQuestion = this.chatModel.call("You are tasked with generating a quiz question. "
                                        + "Topic: '" + quiz.getName() + "'. "
                                        + "Difficulty level: '" + quiz.getQuizLevel().toString().toLowerCase() + "'. "
                                        + "Please create a question based on the provided topic and difficulty level. "
                                        + "Do not add extra information. "
                                        + "For example, if the topic is 'Java Threads' and the level is 'medium', generate a question Ex: 'What is Thread?'.");

                        Question question = Question.builder()
                                        .id(UUID.randomUUID().toString())
                                        .question(quizQuestion)
                                        .quiz(quiz)
                                        .submitOption('0') // Default option as '0'
                                        .build();
                        this.questionService.createQuestion(question, principal);

                        // Generate answers for the question
                        Set<String> answers = generateAnswers(quizQuestion, quiz.getName());

                        // Create a list of options and shuffle them
                        List<Character> options = Arrays.asList('a', 'b', 'c', 'd');
                        Collections.shuffle(options);

                        int index = 0; // Track the index of the shuffled options

                        for (String currentAnswer : answers) {
                                Answer answer = Answer.builder()
                                                .id(UUID.randomUUID().toString())
                                                .answer(currentAnswer)
                                                .answerOption(options.get(index++)) // Assign random but unique option
                                                .isTrue(isTrueOrFalse(quizQuestion, currentAnswer)) // Determine if the
                                                                                                    // answer is correct
                                                .question(question) // Associate the answer with the question
                                                .build();

                                this.answerService.createAnswer(answer, principal); // Save the answer
                        }
                }
                quiz.setReady(true);
                quiz.setTotalScore(0.0);
                this.quizService.updateQuiz(quiz, principal);
                return quiz;
        }

        public boolean isTrueOrFalse(String question, String answer) {
                // Prepare the prompt for the model
                String prompt = "Question: " + question +
                                "\nAnswer: " + answer +
                                "\nImportant task: Check answer accoriding question and Return true or false in one word.";

                // Call the chat model
                String ans = this.chatModel.call(prompt).trim();

                // Validate and return the result
                if ("true".equalsIgnoreCase(ans) || ans.toLowerCase().contains("true")) {
                        return true;
                }
                return false;
        }

        public Set<String> generateAnswers(String question, String quizName) {
                Set<String> answers = new HashSet<>();

                // Generate true answer
                String trueAnswer = this.chatModel.call(
                                "Question: " + question +
                                                "\nTask: Provide a correct answer for this question , between to 1-20 words");
                answers.add(trueAnswer);

                // Generate false answers
                for (int i = 0; i < 3; i++) {
                        String falseAnswer = this.chatModel.call(
                                        "Question: " + question +
                                                        "\nTask: Provide an incorrect answer for this question but should not be outside from "
                                                        + quizName + " topic , between to 1-20 words");
                        answers.add(falseAnswer);
                }

                return answers;
        }

}
