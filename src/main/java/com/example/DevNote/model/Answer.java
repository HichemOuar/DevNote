package com.example.DevNote.model;
import jakarta.persistence.*;


@Entity
@Table(name = "answer")
public class Answer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_answer")
    private Integer ID;
    @Column(name = "answer_content") // On utilise le type string pour les types 'text' de SQL
    private String answercontent;
    @Column(name = "answer_value")
    private boolean answervalue;
    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;
    @OneToOne
    @JoinColumn(name = "ID_question", nullable = false)
    private Question question;
    @OneToOne
    @JoinColumn(name = "ID_session")
    private Session session;

    // Constructeur, getters et setters

    public Answer()
    {
    }

    public Answer(Users user,Question question)
    {
        this.user = user;
        this.question = question;
    }


    public Answer(Users user,Question question,String answercontent)
    {
        this.user = user;
        this.question = question;
        this.answercontent = answercontent;
    }


    public Integer getID()
    {
        return ID;
    }

    public void setID(Integer ID)
    {
        this.ID = ID;
    }

    public String getAnswercontent()
    {
        return answercontent;
    }

    public void setAnswerContent(String answercontent)
    {
        this.answercontent = answercontent;
    }

    public boolean isAnswervalue()
    {
        return answervalue;
    }

    public void setAnswervalue(boolean answervalue)
    {
        this.answervalue = answervalue;
    }

    public Users getUser()
    {
        return user;
    }

    public void setUser(Users user)
    {
        this.user = user;
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
