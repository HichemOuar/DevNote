package com.example.ProjetCDA.repository;

import com.example.ProjetCDA.model.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizzRepository extends JpaRepository<Quizz, Long> {
}