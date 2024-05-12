package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.QuizzRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
public class MappingQuizzTest
{

    @Autowired
    private QuestionRepository questionRepository;
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

    public Question InsertionQuestion()
    {
        Question question = new Question("Qu'est ce que Java?","Un langage de programmation", Access.privé,InsertionUser());
        return question;
    }

    @Test
    public void testCRUDQuizzOK()
    {
        Quizz quizz = InsertionQuizz();
        quizzRepository.save(quizz);
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

        Question question = InsertionQuestion();
        questionRepository.save(question);
        Quizz quizz = InsertionQuizz();
        quizz.getQuestions().add(question);
        quizzRepository.save(quizz);
        question.getQuizzs().add(quizz);
        questionRepository.save(question);
        assertThat(quizzRepository.findById(quizz.getID()).get().getQuestions()).contains(question);
        assertThat(questionRepository.findById(question.getID()).get().getQuizzs()).contains(quizz);

    }


}