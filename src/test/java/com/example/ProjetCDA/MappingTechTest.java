package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.TechRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import com.example.ProjetCDA.service.QuestionService;
import com.example.ProjetCDA.service.TechService;
import com.example.ProjetCDA.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
public class MappingTechTest
{

    @Autowired
    private TechRepository techRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TechService techService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Test
    public void TestCRUDTechOK()
    {
        Tech tech = techService.createTech(LabelTech.Javascript);
        techRepository.save(tech);
        tech.setLabel(LabelTech.CSS);
        techRepository.save(tech);
        assertThat(techRepository.findById(tech.getID())).isPresent().hasValueSatisfying(t -> assertThat(t.getLabel()).isEqualTo(LabelTech.CSS));
        techRepository.delete(tech);
        assertThat(techRepository.findById(tech.getID())).isNotPresent();

    }

    @Test
    @Transactional  // Assurer que les modifications de collection sont persistées.
    public void testRelationTechQuestions() {
        Tech tech = techService.createTech(LabelTech.Javascript);
        techRepository.save(tech);
        Users user = userService.createUser("testusername","username@gmail.com", "password",Role.Apprenant);
        usersRepository.save(user);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        questionRepository.save(question);
        question.getTechs().add(tech);
        questionRepository.save(question);
        tech.getQuestions().add(question);
        techRepository.save(tech);
        // Utiliser les repositories pour tester la présence des relations.
        assertThat(techRepository.findById(tech.getID()).get().getQuestions()).contains(question);
        assertThat(questionRepository.findById(question.getID()).get().getTechs()).contains(tech);
    }


}