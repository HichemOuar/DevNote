package com.example.DevNote.service;
import com.example.DevNote.DTO.CreateQuestionDTO;

import com.example.DevNote.model.Access;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Transactional
    public Question createQuestion(CreateQuestionDTO dto, Users user) throws Exception  // NB: très important : Quand tu définis une exception dans une méthode, quand tu appelles cette méthode,
    // il faut utiliser un try catch ou un autre moyen de gérer les exceptions
    {
        if (questionRepository.existsByContentAndUser(dto.getContent(), user))
        {
            throw new Exception("Cette question existe déjà pour cet utilisateur.");
        }

        Access access = dto.getAccess(); // Détermine si la question est publique ou privée
        Question question = new Question(dto.getContent(), dto.getExpectedanswer(), access, user);
        questionRepository.save(question);
        return question;

    }

    public List<Question> getQuestionsForUserAndPublic(Users user) {
        return questionRepository.findByUserOrAccess(user, Access.publique);
    }
}