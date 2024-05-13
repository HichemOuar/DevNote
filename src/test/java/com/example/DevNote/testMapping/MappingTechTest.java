package com.example.DevNote.testMapping;

import com.example.DevNote.model.*;
import com.example.DevNote.repository.QuestionRepository;
import com.example.DevNote.repository.TechRepository;
import com.example.DevNote.repository.UsersRepository;
import com.example.DevNote.service.QuestionService;
import com.example.DevNote.service.TechService;
import com.example.DevNote.service.UserService;
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
        Users user = userService.createUser("testusername","username@gmail.com", "password",Role.Apprenant);
        Question question = questionService.createQuestionMinimum("Qu'est ce que Java?","Un langage de programmation", Access.privé,user);
        question.getTechs().add(tech);
        questionRepository.save(question);
        tech.getQuestions().add(question);
        techRepository.save(tech);
        // Utiliser les repositories pour tester la présence des relations.
        assertThat(techRepository.findById(tech.getID()).get().getQuestions()).contains(question);
        assertThat(questionRepository.findById(question.getID()).get().getTechs()).contains(tech);
    }


}