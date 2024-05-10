package com.example.ProjetCDA.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "comments")
public class Comment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_comment")
    private Long ID;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "date_of_post", nullable = false)
    private LocalDateTime dateofpost;
    @Column(name = "last_updated")
    private LocalDateTime lastupdated;
    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;


    // Constructeur, getters et setters

    public Comment()
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


}
