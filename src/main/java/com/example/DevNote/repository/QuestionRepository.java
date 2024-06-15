package com.example.DevNote.repository;

import com.example.DevNote.model.Access;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    boolean existsByContentAndUser(String content, Users user); // vérifie l'existence d'une question par intitulé et utilisateur simultanémennt ( car deux utilisateurs différents peuvent
    // avoir des questions avec le même intitulé, mais un utilisateur ne peut pas avoir deux questions avec le même intitulé)

    @Query("SELECT q FROM Question q WHERE q.user = :user OR q.access = :access") //  est une requête préparée JPQL (Java Persistence Query Langage) et elle est protégée contre les
        // injections SQL. Spring Data JPA utilise automatiquement les paramètres de requête préparés.
    List<Question> findByUserOrAccess(Users user, Access access); // cherche la liste de questions pour un utilisateur donné ou pour une accessiblité donnée. On va l'utiliser dans la page
    // searchquestion
}