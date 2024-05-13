package com.example.ProjetCDA.service;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;


    public Comment createCommentMinimum(String content, LocalDateTime dateofpost, Users user, Question question) {
        Comment comment = new Comment(content,dateofpost,user,question);
        commentRepository.save(comment);
        return comment;
    }

}