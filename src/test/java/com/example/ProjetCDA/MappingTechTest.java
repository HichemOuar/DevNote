package com.example.ProjetCDA;

import com.example.ProjetCDA.model.*;
import com.example.ProjetCDA.repository.QuestionRepository;
import com.example.ProjetCDA.repository.TechRepository;
import com.example.ProjetCDA.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
public class MappingTechTest
{

    @Autowired
    private TechRepository techRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private QuestionRepository questionRepository;


    public Users InsertionUser() // on doit créer et persister un user en base, car l'id_user est un champs obligatoire de la table question
    {
        Users user = new Users("utilisateur", "mail@example.com", "password", Role.Apprenant);
        usersRepository.save(user);
        return user;
    }
    public Question InsertionQuestion()
    {
        Question question = new Question("Qu'est ce que Java?","Un langage de programmation",Access.privé,InsertionUser());
        questionRepository.save(question);
        return question;
    }
    public Tech InsertionTech()
    {
        Tech tech = new Tech();
        tech.setLabel(LabelTech.Java);
        techRepository.save(tech);
        return tech;
    }
    @Test
    public void TestCRUDTechOK()
    {
        Tech tech=InsertionTech();
        assertThat(techRepository.findById(tech.getID())).isPresent();
        tech.setLabel(LabelTech.CSS);
        techRepository.save(tech);
        assertThat(techRepository.findById(tech.getID())).isPresent().hasValueSatisfying(t -> assertThat(t.getLabel()).isEqualTo(LabelTech.CSS));
        techRepository.delete(tech);
        assertThat(techRepository.findById(tech.getID())).isNotPresent();

    }

    @Test
    @Transactional  // Assurer que les modifications de collection sont persistées.
    public void testRelationTechQuestions() {
        Tech tech = InsertionTech();
        techRepository.save(tech);
        Question question = InsertionQuestion();
        question.getTechs().add(tech);
        questionRepository.save(question);
        tech.getQuestions().add(question);
        techRepository.save(tech);  // Sauvegarder les modifications des collections.
        // Utiliser les repositories pour tester la présence des relations.
        assertThat(techRepository.findById(tech.getID()).get().getQuestions()).contains(question);
        assertThat(questionRepository.findById(question.getID()).get().getTechs()).contains(tech);
    }


}