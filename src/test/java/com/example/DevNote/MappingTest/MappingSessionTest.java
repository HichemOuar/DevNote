package com.example.DevNote.MappingTest;

import com.example.DevNote.model.*;
import com.example.DevNote.repository.*;
import com.example.DevNote.service.QuizzService;
import com.example.DevNote.service.SessionService;
import com.example.DevNote.service.UserService;
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
        Users user = userService.createUserTestMapping("testusername","username@gmail.com", "password",Role.Apprenant);
        Quizz quizz = quizzService.createQuizzMinimum("Titre Quizz",Access.privé,user);
        Session session = sessionService.createSessionMinimum(user,quizz);
        assertThat(sessionRepository.findById(session.getID())).isPresent();
        sessionRepository.delete(session);
        assertThat(sessionRepository.findById(session.getID())).isNotPresent();

    }


}