package com.example.DevNote.repository;

import com.example.DevNote.model.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizzRepository extends JpaRepository<Quizz, Integer> {
}