package com.example.ProjetCDA.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_session")
    private Long ID;

    @Column(name = "last_updated")
    private LocalDateTime lastupdated;

    @Column(name = "score")
    private int score;

    @Column(name = "time_spent")
    private int timespent;

    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ID_quizz", nullable = false)
    private Quizz quizz;

    // Constructeur, getters et setters

    public Session()
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getLastupdated()
    {
        return lastupdated;
    }

    public void setLastupdated(LocalDateTime lastupdated)
    {
        this.lastupdated = lastupdated;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getTimespent()
    {
        return timespent;
    }

    public void setTimespent(int timespent)
    {
        this.timespent = timespent;
    }


    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

}
