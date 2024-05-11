package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
public class MappingCommentTest
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

    public Comment InsertionComment()
    {
        Comment comment = new Comment();
        comment.setContent("Voici mon commentaire très utile!");
        comment.setDateofpost(LocalDateTime.of(2024, 5, 10, 15, 30));
        comment.setUser(InsertionUser());
        entityManager.persist(comment);
        entityManager.flush();
        return comment;
    }

    @Test
    public void testInsertionCommentOK()
    {
        Comment comment= InsertionComment();
        Comment commentbdd = entityManager.find(Comment.class, comment.getID());
        assertThat(commentbdd).isEqualTo(comment);
    }

}