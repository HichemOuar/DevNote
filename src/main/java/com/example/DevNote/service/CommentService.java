package com.example.DevNote.service;

import com.example.DevNote.model.*;
import com.example.DevNote.repository.CommentRepository;
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