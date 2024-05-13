package com.example.DevNote.service;
import com.example.DevNote.model.Access;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Méthode pour créer une question avec le minimum de champs obligatoires possibles
    public Question createQuestionMinimum(String content, String expectedanswer, Access access, Users user) {
        Question question = new Question(content, expectedanswer, access, user);
        questionRepository.save(question);
        return question;
    }
}