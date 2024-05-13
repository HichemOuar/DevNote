package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.AnswerRepository;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import com.example.ProjetCDA.service.AnswerService;
import com.example.ProjetCDA.service.QuestionService;
import com.example.ProjetCDA.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class MappingAnswerTest
{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;


    @Test
    public void testCRUDAnswerOK()
    {
        Users user = userService.createUser("utilisateur", "mail@example.com", "password", Role.Apprenant);
        usersRepository.save(user);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        questionRepository.save(question);
        Answer answer = answerService.createAnswerWithContent(user,question,"Ma réponse");
        answerRepository.save(answer);
        assertThat(answerRepository.findById(answer.getID())).isPresent();
        answer.setAnswercontent("Ma réponse 2");
        answerRepository.save(answer);
        assertThat(answerRepository.findById(answer.getID())).isPresent().hasValueSatisfying(a -> assertThat(a.getAnswercontent()).isEqualTo("Ma réponse 2"));
        answerRepository.delete(answer);
        assertThat(answerRepository.findById(answer.getID())).isNotPresent();
    }


}