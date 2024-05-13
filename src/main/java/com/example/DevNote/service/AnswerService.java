package com.example.DevNote.service;
import com.example.DevNote.model.Answer;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.AnswerRepository;
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