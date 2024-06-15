package com.example.DevNote.repository;

import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    boolean existsByContentAndUser(String content, Users user); // vérifie l'existence d'une question par intitulé et utilisateur simultanémennt ( car deux utilisateurs différents peuvent
    // avoir des questions avec le même intitulé, mais un utilisateur ne peut pas avoir deux questions avec le même intitulé)
}