package com.example.ProjetCDA.service;
import com.example.ProjetCDA.model.Access;
import com.example.ProjetCDA.model.Question;
import com.example.ProjetCDA.model.Users;
import com.example.ProjetCDA.repository.QuestionRepository;
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