package com.example.DevNote.repository;

import com.example.DevNote.model.Tech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechRepository extends JpaRepository<Tech, Integer> {
}