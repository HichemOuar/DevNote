package com.example.DevNote.repository;

import com.example.DevNote.model.Tech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechRepository extends JpaRepository<Tech, Integer> {
}