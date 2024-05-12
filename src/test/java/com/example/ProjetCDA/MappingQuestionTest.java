package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class MappingQuestionTest
{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UsersRepository usersRepository;


    public Users InsertionUser()
    {
        Users user = new Users("utilisateur", "mail@example.com", "password", Role.Apprenant);
        usersRepository.save(user);
        return user;
    }
    public Question InsertionQuestion()
    {
        Question question = new Question("Qu'est ce que Java?","Un langage de programmation", Access.privé,InsertionUser());
        return question;
    }
    @Test
    public void testCRUDQuestionOK()
    {
        Question question= InsertionQuestion();
        questionRepository.save(question);
        assertThat(questionRepository.findById(question.getID())).isPresent();
        question.setContent("Nouvelle question");
        questionRepository.save(question);
        assertThat(questionRepository.findById(question.getID())).isPresent().hasValueSatisfying(q -> assertThat(q.getContent()).isEqualTo("Nouvelle question"));
        questionRepository.delete(question);
        assertThat(questionRepository.findById(question.getID())).isNotPresent();
    }


}