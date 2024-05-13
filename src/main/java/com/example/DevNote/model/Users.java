package com.example.DevNote.model;

import jakarta.persistence.*;

@Entity // Cette annotation indique que cette classe est une entité JPA
@Table(name = "users") // signifie que cette classe est mappée à la table Users en base de données
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Cette annotation est utilisée pour spécifier la stratégie de génération de la valeur des clés primaires. Elle automatise la
                                                       // création de valeurs uniques pour chaque entitées. IDENTITY est une des stratégies de génération qui indique que la clé primaire
                                                      //  sera générée par la base de données,
    @Column(name = "ID_USER")
    private Long ID;

    @Column(name = "Username", nullable = false) // Ces champs sont mappés aux colonnes correspondantes en base de données.IL EST NECESSAIRE d'AJOUTER nullable=false si le champs ne
                                                //  peut pas être nul en bdd, sauf pour l'ID car c'est autogénéré par Spring JPA avec l'annotation GeneratedValue
    private String username;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING) // Role est une énumération: On la définit dans le fichier model> Role (Si l'énumération a le potentiel d'être utilisée par plusieurs classes ou pour
                                //  représenter une logique métier au-delà d'une seule entité, il est préférable de la placer dans son propre fichier.
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
