package com.example.ProjetCDA.service;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;


    public Session createSessionMinimum(Users user, Quizz quizz) {
        Session session = new Session(user,quizz);
        sessionRepository.save(session);
        return session;
    }

}