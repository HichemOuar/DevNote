package com.example.ProjetCDA.repository;

import com.example.ProjetCDA.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}