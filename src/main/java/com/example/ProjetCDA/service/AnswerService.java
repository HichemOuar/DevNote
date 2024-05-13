package com.example.ProjetCDA.service;
import com.example.ProjetCDA.model.Answer;
import com.example.ProjetCDA.model.Question;
import com.example.ProjetCDA.model.Users;
import com.example.ProjetCDA.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;


    public Answer createAnswerMinimum(Users user, Question question) {
        Answer answer = new Answer(user,question);
        answerRepository.save(answer);
        return answer;
    }

    public Answer createAnswerWithContent(Users user, Question question, String answercontent) {
        Answer answer = new Answer(user,question,answercontent);
        answerRepository.save(answer);
        return answer;
    }
}