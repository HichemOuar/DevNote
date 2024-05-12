package com.example.ProjetCDA.repository;

import com.example.ProjetCDA.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}