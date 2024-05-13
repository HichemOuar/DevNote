package com.example.DevNote.model;
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
    @JoinTable( // Cette annotation est utilisée pour spécifier la table de jointure utilisée pour la relation many-to-many
            name = "question_quizz", // Le nom de la table de jointure dans la base de données.
            joinColumns = @JoinColumn(name = "ID_quizz"), // Spécifie la colonne de jointure dans la table de jointure pour l'entité propriétaire de la relation (ici Question)
            inverseJoinColumns = @JoinColumn(name = "ID_question")) // Spécifie la colonne de jointure dans la table de jointure pour l'entité non- propriétaire de la relation

    private Set<Question> questions = new HashSet<>(); //c'est une COLLECTION DE questions. elle permet de gérer facilement les questions associées à chaque quizz Le choix de Set
                                                      // plutôt que List est généralement pour éviter les doublons, garantissant que la même question ne peut pas être associée
                                                     //  plusieurs fois au même quiz.HashSet est une implémentation de l'interface Set qui utilise une table de hachage pour stocker les
                                                    //   éléments. Elle ne permet pas de doublons, ce qui est idéal pour les relations many-to-many


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
