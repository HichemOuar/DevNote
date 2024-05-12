package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.AnswerRepository;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class MappingAnswerTest
{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AnswerRepository answerRepository;

    public Users InsertionUser()
    {
        Users user = new Users("utilisateur", "mail@example.com", "password", Role.Apprenant);
        usersRepository.save(user);
        return user;
    }

    public Question InsertionQuestion()
    {
        Question question = new Question("Qu'est ce que Java?","Un langage de programmation", Access.privé,InsertionUser());
        questionRepository.save(question);
        return question;
    }

    public Answer InsertionAnswer()
    {
        Answer answer = new Answer(InsertionUser(),InsertionQuestion(),"ma réponse");
        answerRepository.save(answer);
        return answer;
    }

    @Test
    public void testCRUDAnswerOK()
    {
        Answer answer= InsertionAnswer();
        answerRepository.save(answer);
        assertThat(answerRepository.findById(answer.getID())).isPresent();
        answer.setAnswercontent("Nouvelle réponse");
        answerRepository.save(answer);
        assertThat(answerRepository.findById(answer.getID())).isPresent().hasValueSatisfying(a -> assertThat(a.getAnswercontent()).isEqualTo("Nouvelle réponse"));
        answerRepository.delete(answer);
        assertThat(answerRepository.findById(answer.getID())).isNotPresent();
    }


}