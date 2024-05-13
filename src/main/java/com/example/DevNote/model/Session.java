package com.example.DevNote.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_session")
    private Long ID;

    @Column(name = "last_updated") // dans les versions récentes de java, on utilise le type LocalDateTime pour représenter une date
    private LocalDateTime lastupdated;

    @Column(name = "score")
    private int score;

    @Column(name = "time_spent")
    private int timespent;

    @ManyToOne //Pour représenter une relation avec une clé étrangère, on peut utiliser les annotations @ManyToOne ou @OneToOne. Cette annotation est utilisée quand plusieurs instances
              // de l'entité source (ici, Session) peuvent être associées à une seule instance de l'entité cible (ici, Users). C'est ce qu'on appelle une relation "many-to-one".
             //  Si par exemple un utilisateur peut avoir plusieurs sessions, chaque entité Session aura une clé étrangère pointant vers un utilisateur spécifique.

    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ID_quizz", nullable = false)
    private Quizz quizz;

    // Constructeur, getters et setters

    public Session()
    {
    }
    public Session(Users user, Quizz quizz)
    {
        this.user = user;
        this.quizz = quizz;
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
