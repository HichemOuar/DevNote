package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.CommentRepository;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
public class MappingCommentTest
{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CommentRepository commentRepository;

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

    public Comment InsertionComment()
    {
        Comment comment = new Comment("Voici mon commentaire très utile!",LocalDateTime.of(2024, 5, 10, 15, 30),InsertionUser(),InsertionQuestion());
        commentRepository.save(comment);
        return comment;
    }

    @Test
    public void testCRUDCommentOK()
    {
        Comment comment= InsertionComment();
        commentRepository.save(comment);
        assertThat(commentRepository.findById(comment.getID())).isPresent();
        comment.setContent("Nouveau commentaire");
        commentRepository.save(comment);
        assertThat(commentRepository.findById(comment.getID())).isPresent().hasValueSatisfying(q -> assertThat(q.getContent()).isEqualTo("Nouveau commentaire"));
        commentRepository.delete(comment);
        assertThat(commentRepository.findById(comment.getID())).isNotPresent();
    }

}