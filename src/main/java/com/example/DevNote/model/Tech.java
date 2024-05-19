package com.example.DevNote.model;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "techs")
public class Tech
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_tech")
    private Integer ID;
    @Enumerated(EnumType.STRING)
    @Column(name = "Label", nullable = false)
    private LabelTech label;

    @ManyToMany(mappedBy = "techs")
    private Set<Question> questions = new HashSet<>();


    // Constructeur, getters et setters

    public Tech()
    {
    }

    public Tech(LabelTech label)
    {
        this.label = label;
    }


    public Integer getID()
    {
        return ID;
    }

    public void setID(Integer ID)
    {
        this.ID = ID;
    }

    public LabelTech getLabel() {
        return label;
    }

    public void setLabel(LabelTech label) {
        this.label = label;
    }


    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }


}
