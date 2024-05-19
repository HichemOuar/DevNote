package com.example.DevNote.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "comments")
public class Comment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Integer ID;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "date_of_post", nullable = false)
    private LocalDateTime dateofpost;
    @Column(name = "last_updated")
    private LocalDateTime lastupdated;
    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ID_question", nullable = false)
    private Question question;


    // Constructeur, getters et setters

    public Comment()
    {
    }

    public Comment(String content, LocalDateTime dateofpost, Users user, Question question)
    {
        this.content = content;
        this.dateofpost = dateofpost;
        this.user = user;
        this.question = question;
    }


    public Integer getID()
    {
        return ID;
    }

    public void setID(Integer ID)
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

    public LocalDateTime getDateofpost()
    {
        return dateofpost;
    }

    public void setDateofpost(LocalDateTime dateofpost)
    {
        this.dateofpost = dateofpost;
    }

    public LocalDateTime getLastupdated()
    {
        return lastupdated;
    }

    public void setLastupdated(LocalDateTime lastupdated)
    {
        this.lastupdated = lastupdated;
    }

    public Users getUser()
    {
        return user;
    }

    public void setUser(Users user)
    {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


}
