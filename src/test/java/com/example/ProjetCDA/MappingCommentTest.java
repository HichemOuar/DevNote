package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.CommentRepository;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import com.example.ProjetCDA.service.CommentService;
import com.example.ProjetCDA.service.QuestionService;
import com.example.ProjetCDA.service.QuizzService;
import com.example.ProjetCDA.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
public class MappingCommentTest
{
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuizzService quizzService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Test
    public void testCRUDCommentOK()
    {
        Users user = userService.createUser("testusername","username@gmail.com", "password",Role.Apprenant);
        usersRepository.save(user);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        questionRepository.save(question);
        Comment comment= commentService.createCommentMinimum("Mon commentaire",LocalDateTime.of(2024, 5, 10, 15, 30),user,question);
        commentRepository.save(comment);
        assertThat(commentRepository.findById(comment.getID())).isPresent();
        comment.setContent("Nouveau commentaire");
        commentRepository.save(comment);
        assertThat(commentRepository.findById(comment.getID())).isPresent().hasValueSatisfying(q -> assertThat(q.getContent()).isEqualTo("Nouveau commentaire"));
        commentRepository.delete(comment);
        assertThat(commentRepository.findById(comment.getID())).isNotPresent();
    }

}