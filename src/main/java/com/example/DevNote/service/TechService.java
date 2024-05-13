package com.example.DevNote.service;

import com.example.DevNote.model.*;
import com.example.DevNote.repository.TechRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechService {

    @Autowired
    private TechRepository techRepository;


    public Tech createTech(LabelTech label) {
        Tech tech = new Tech(label);
        techRepository.save(tech);
        return tech;
    }

}