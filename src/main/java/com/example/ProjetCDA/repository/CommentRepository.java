package com.example.ProjetCDA.repository;

import com.example.ProjetCDA.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}