package com.ahad.serviceImpl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahad.entities.Question;
import com.ahad.entities.User;
import com.ahad.repository.QuestionRepository;
import com.ahad.services.AnswerService;
import com.ahad.services.QuestionService;
import com.ahad.services.UserService;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserService userService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question createQuestion(Question question, Principal principal) {
        return this.questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question question, Principal principal) {
        return this.questionRepository.save(question);
    }

    @Override
    public Question getQuestion(String id, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        Question question = this.questionRepository.findById(id).get();
        return (user.getQuizzes().contains(question.getQuiz())) ? question : null;
    }

    @Override
    public void deleteQuestion(String id, Principal principal) {
        this.questionRepository.deleteById(id);
    }

    @Override
    public List<Question> getQuestionsByQuizId(String quizId) {

        List<Question> questions = this.questionRepository.findAllQuestionByQuizId(quizId);

        for (Question q : questions) {
            q.setAnswers(this.answerService.findAllByQuestionId(q.getId()));
        }
        return questions;
    }

    @Override
    public Question getNextQuestion(String quizId, int currentQuestionNumber) {
        List<Question> questions = this.getQuestionsByQuizId(quizId);
        if (currentQuestionNumber < questions.size()) {
            return questions.get(currentQuestionNumber);
        }
        return null; // No more questions available
    }

}
