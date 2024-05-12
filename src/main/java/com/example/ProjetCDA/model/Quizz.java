package com.example.ProjetCDA.model;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quizz")
public class Quizz
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_quizz")
    private Long ID;
    @Column(name = "title", nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "access", nullable = false)
    private Access access;
    @Column(name = "time_limit")
    private int timelimit;
    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;
    @ManyToMany
    @JoinTable(name = "question_quizz", joinColumns = @JoinColumn(name = "ID_quizz"), inverseJoinColumns = @JoinColumn(name = "ID_question"))
    private Set<Question> questions = new HashSet<>();


    // Constructeur, getters et setters

    public Quizz()
    {

    }

    public Quizz(String title, Access access, Users user)
    {
     this.title= title;
     this.access = access;
     this.user= user;
    }

    public Long getID()
    {
        return ID;
    }

    public void setID(Long ID)
    {
        this.ID = ID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Access getAccess()
    {
        return access;
    }

    public void setAccess(Access access)
    {
        this.access = access;
    }

    public int getTimelimit()
    {
        return timelimit;
    }

    public void setTimelimit(int timelimit)
    {
        this.timelimit = timelimit;
    }

    public Users getUser()
    {
        return user;
    }

    public void setUser(Users user)
    {
        this.user = user;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

}
