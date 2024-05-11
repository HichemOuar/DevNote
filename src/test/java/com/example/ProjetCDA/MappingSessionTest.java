package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MappingSessionTest
{

    @Autowired
    private TestEntityManager entityManager;

    public Users InsertionUser()
    {
        Users user = new Users();
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setPassword("password");
        user.setRole(Role.Apprenant);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
    public Quizz InsertionQuizz()
    {
        Quizz quizz = new Quizz();
        quizz.setTitle("Quizz sur Java");
        quizz.setAccess(Access.privé);
        quizz.setUser(InsertionUser());
        entityManager.persist(quizz);
        entityManager.flush();
        return quizz;
    }

    public Session InsertionSession()
    {
        Session session = new Session();
        session.setQuizz(InsertionQuizz());
        session.setUser(InsertionUser());
        entityManager.persist(session);
        entityManager.flush();
        return session;
    }


    @Test
    public void testInsertionSessionOK()
    {
        Session session= InsertionSession();
        Session sessionbdd = entityManager.find(Session.class, session.getID());
        assertThat(sessionbdd).isEqualTo(session);
    }


}