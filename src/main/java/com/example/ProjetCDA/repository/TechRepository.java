package com.example.ProjetCDA.repository;

import com.example.ProjetCDA.model.Tech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechRepository extends JpaRepository<Tech, Long> {
}