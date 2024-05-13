package com.example.ProjetCDA.service;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuizzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizzService {

    @Autowired
    private QuizzRepository quizzRepository;


    public Quizz createQuizzMinimum(String title, Access access, Users user) {
        Quizz quizz = new Quizz(title,access,user);
        quizzRepository.save(quizz);
        return quizz;
    }

}