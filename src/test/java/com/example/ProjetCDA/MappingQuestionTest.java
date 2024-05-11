package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class MappingQuestionTest
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
        return user;
    }
    public Question InsertionQuestion()
    {
        Question question = new Question();
        question.setContent("Qu'est ce que Java?");
        question.setExpectedanswer("Un langage de programmation");
        question.setAccess(Access.privé);
        question.setUser(InsertionUser());
        entityManager.persist(question);
        return question;
    }
    @Test
    public void testInsertionQuestionOK()
    {
        Question question= InsertionQuestion();
        Question questionbdd = entityManager.find(Question.class, question.getID());
        assertThat(questionbdd).isEqualTo(question);
    }


}