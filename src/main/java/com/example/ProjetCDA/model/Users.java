package com.example.ProjetCDA.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    private Long ID;

    @Column(name = "Username", nullable = false)
    private String username;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private Role role;

    // Constructeur, getters et setters

    public Users() // Instantiation par réflexion : Certains frameworks comme JPA, Hibernate ou Spring utilisent la réflexion pour créer des instances de classes. Ces frameworks
                  // nécessitent souvent un constructeur vide pour pouvoir instancier la classe sans faire d'hypothèses sur les paramètres nécessaires. Si un constructeur par défaut
                 // n'est pas explicitement défini et qu'un autre constructeur avec des arguments est présent, le compilateur Java ne générera pas automatiquement ce constructeur vide.
    {
    }

    public Users(String username, String email, String password, Role role)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId()
    {
        return ID;
    }

    public void setId(Long ID)
    {
        this.ID = ID;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
