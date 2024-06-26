package com.example.DevNote.MappingTest;

import com.example.DevNote.model.*;
import com.example.DevNote.repository.AnswerRepository;
import com.example.DevNote.repository.QuestionRepository;
import com.example.DevNote.repository.UsersRepository;
import com.example.DevNote.service.*;
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

    @Autowired
    private SessionService sessionService;

    @Autowired
    private QuizzService quizzService;

    @Test
    public void testCRUDAnswerOK()
    {
        Users user = userService.createUserTestMapping("utilisateur", "mail@example.com", "password", Role.Apprenant);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        Quizz quizz = quizzService.createQuizzMinimum("Quizz de test",Access.privé,user);
        Session session = sessionService.createSessionMinimum(user,quizz);
        Answer answer = answerService.createAnswerWithContent(user,question,session,"Ma réponse");
        assertThat(answerRepository.findById(answer.getID())).isPresent();
        answer.setAnswerContent("Ma réponse 2");
        answerRepository.save(answer);
        assertThat(answerRepository.findById(answer.getID())).isPresent().hasValueSatisfying(a -> assertThat(a.getAnswercontent()).isEqualTo("Ma réponse 2"));
        answerRepository.delete(answer);
        assertThat(answerRepository.findById(answer.getID())).isNotPresent();
    }


}