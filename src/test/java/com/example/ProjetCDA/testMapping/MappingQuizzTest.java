package com.example.ProjetCDA.testMapping;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.QuizzRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import com.example.ProjetCDA.service.QuestionService;
import com.example.ProjetCDA.service.QuizzService;
import com.example.ProjetCDA.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
public class MappingQuizzTest
{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizzService quizzService;

    @Autowired
    private UserService userService;


    @Test
    public void testCRUDQuizzOK()
    {

        Users user = userService.createUser("testusername","username@gmail.com", "password",Role.Apprenant);
        Quizz quizz = quizzService.createQuizzMinimum("Titre Quizz",Access.privé,user);
        assertThat(quizzRepository.findById(quizz.getID())).isPresent();
        quizz.setTitle("New title quizz");
        quizzRepository.save(quizz);
        assertThat(quizzRepository.findById(quizz.getID())).isPresent().hasValueSatisfying(q -> assertThat(q.getTitle()).isEqualTo("New title quizz"));
        quizzRepository.delete(quizz);
        assertThat(quizzRepository.findById(quizz.getID())).isNotPresent();
    }

    @Test
    @Transactional
    public void testRelationQuizzQuestions() {

        Users user = userService.createUser("testusername","username@gmail.com", "password",Role.Apprenant);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        Quizz quizz = quizzService.createQuizzMinimum("Titre Quizz",Access.privé,user);
        quizz.getQuestions().add(question);
        quizzRepository.save(quizz);
        question.getQuizzs().add(quizz);
        questionRepository.save(question);
        assertThat(quizzRepository.findById(quizz.getID()).get().getQuestions()).contains(question);
        assertThat(questionRepository.findById(question.getID()).get().getQuizzs()).contains(quizz);

    }


}