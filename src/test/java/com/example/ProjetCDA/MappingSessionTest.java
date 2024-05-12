package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MappingSessionTest
{

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private UsersRepository usersRepository;

    public Users InsertionUser()
    {
        Users user = new Users("utilisateur", "mail@example.com", "password", Role.Apprenant);
        usersRepository.save(user);
        return user;
    }
    public Quizz InsertionQuizz()
    {
        Quizz quizz= new Quizz("Quizz sur Java",Access.privé,InsertionUser());
        quizzRepository.save(quizz);
        return quizz;

    }

    public Session InsertionSession()
    {
        Session session = new Session(InsertionUser(),InsertionQuizz());
        sessionRepository.save(session);
        return session;
    }


    @Test
    public void TestCRUDSessionOK()
    {
        Session session = InsertionSession();
        sessionRepository.save(session);
        assertThat(sessionRepository.findById(session.getID())).isPresent();
        sessionRepository.delete(session);
        assertThat(sessionRepository.findById(session.getID())).isNotPresent();

    }


}