package com.example.ProjetCDA.model;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "question")
public class Question
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_question")
    private Long ID;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "expected_answer", nullable = false)
    private String expectedanswer;
    @Column(name = "visual_infos")
    private byte[] visualinfos;
    @Enumerated(EnumType.STRING)
    @Column(name = "access", nullable = false)
    private Access access;
    @Column(name = "time_limit")
    private int timelimit;
    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;
    @ManyToMany(mappedBy = "questions")
    private Set<Quizz> quizzs = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "techs_question", joinColumns = @JoinColumn(name = "ID_question"), inverseJoinColumns = @JoinColumn(name = "ID_tech"))
    private Set<Tech> techs = new HashSet<>();

    // Constructeur, getters et setters

    public Question()
    {
    }

    public Long getID()
    {
        return ID;
    }

    public void setID(Long ID)
    {
        this.ID = ID;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getExpectedanswer()
    {
        return expectedanswer;
    }

    public void setExpectedanswer(String expectedanswer)
    {
        this.expectedanswer = expectedanswer;
    }

    public byte[] getVisualinfos()
    {
        return visualinfos;
    }

    public void setVisualinfos(byte[] visualinfos)
    {
        this.visualinfos = visualinfos;
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

    public Set<Quizz> getQuizzs() {
        return quizzs;
    }

    public void setQuizzs(Set<Quizz> quizzs) {
        this.quizzs = quizzs;
    }

    public Set<Tech> getTechs() {
        return techs;
    }

    public void setTechs(Set<Tech> techs) {
        this.techs = techs;
    }


}
