package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class MappingQuizzTest
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

    public Quizz InsertionQuizz()
    {
        Quizz quizz = new Quizz();
        quizz.setTitle("Quizz sur Java");
        quizz.setAccess(Access.privé);
        quizz.setUser(InsertionUser());
        entityManager.persist(quizz);
        return quizz;
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
    public void testInsertionQuizzOK()
    {
        Quizz quizz= InsertionQuizz();
        Quizz quizzbdd = entityManager.find(Quizz.class, quizz.getID());
        assertThat(quizzbdd).isEqualTo(quizz);
    }

    @Test
    public void testRelationTechQuestions() {

        Quizz quizz = InsertionQuizz();
        Question question = InsertionQuestion();
        quizz.getQuestions().add(question);
        question.getQuizzs().add(quizz);
        entityManager.flush();
        Question questionbdd = entityManager.find(Question.class, question.getID());
        assertTrue(questionbdd.getQuizzs().contains(quizz));
        Quizz quizzbdd = entityManager.find(Quizz.class, quizz.getID());
        assertTrue(quizzbdd.getQuestions().contains(question));
    }


}