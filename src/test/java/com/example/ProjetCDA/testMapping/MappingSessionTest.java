package com.example.ProjetCDA.testMapping;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.*;
import com.example.ProjetCDA.service.QuizzService;
import com.example.ProjetCDA.service.SessionService;
import com.example.ProjetCDA.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MappingSessionTest
{

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private QuizzService quizzService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;

    @Test
    public void TestCRUDSessionOK()
    {
        Users user = userService.createUser("testusername","username@gmail.com", "password",Role.Apprenant);
        Quizz quizz = quizzService.createQuizzMinimum("Titre Quizz",Access.privé,user);
        Session session = sessionService.createSessionMinimum(user,quizz);
        assertThat(sessionRepository.findById(session.getID())).isPresent();
        sessionRepository.delete(session);
        assertThat(sessionRepository.findById(session.getID())).isNotPresent();

    }


}