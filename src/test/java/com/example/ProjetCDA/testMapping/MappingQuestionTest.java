package com.example.ProjetCDA.testMapping;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import com.example.ProjetCDA.service.QuestionService;
import com.example.ProjetCDA.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class MappingQuestionTest
{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Test
    public void testCRUDQuestionOK()
    {
        Users user = userService.createUser("utilisateur", "mail@example.com", "password", Role.Apprenant);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        assertThat(questionRepository.findById(question.getID())).isPresent();
        question.setContent("Nouvelle question");
        questionRepository.save(question);
        assertThat(questionRepository.findById(question.getID())).isPresent().hasValueSatisfying(q -> assertThat(q.getContent()).isEqualTo("Nouvelle question"));
        questionRepository.delete(question);
        assertThat(questionRepository.findById(question.getID())).isNotPresent();
    }


}