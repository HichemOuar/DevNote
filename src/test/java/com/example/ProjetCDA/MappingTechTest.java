package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class MappingTechTest
{

    @Autowired
    private TestEntityManager entityManager;

    public Tech InsertionTech()
    {
        Tech tech = new Tech();
        tech.setLabel(LabelTech.Java);
        entityManager.persist(tech);
        entityManager.flush();
        return tech;

    }

    public Users InsertionUser() // on doit créer et persister un user en base, car l'id_user est un champs obligatoire de la table question
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
    public void testInsertionTechOK()
    {
        Tech tech= InsertionTech();
        Tech techbdd = entityManager.find(Tech.class, tech.getID());
        assertThat(techbdd).isEqualTo(tech);
    }

    @Test
    public void testRelationTechQuestions() {  // Test vérifiant la relation entre les tables tech et question, puisqu'il s'agit d'une relation n-n

        Tech tech = InsertionTech();
        Question question = InsertionQuestion();
        question.getTechs().add(tech);
        tech.getQuestions().add(question);
        entityManager.flush();
        Tech techbdd = entityManager.find(Tech.class, tech.getID());
        assertTrue(techbdd.getQuestions().contains(question));
        Question questionbdd = entityManager.find(Question.class, question.getID());
        assertTrue(questionbdd.getTechs().contains(tech));
    }


}