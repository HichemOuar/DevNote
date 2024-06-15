package com.example.DevNote.model;
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
    private Integer ID;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "expected_answer", nullable = false,columnDefinition = "TEXT")  // en base de données, expected_answer est de type medium text ( accepte jusqu'à  65,535 caractères)
    private String expectedanswer;
    @Column(name = "visual_infos") // Pour les champs de type mediumblob dans une base de données MySQL qui sont utilisés pour stocker de grands objets binaires (comme des images, des
                                  //  fichiers audio, ou d'autres données binaires), le type Java approprié est byte[]. Ce type permet de stocker des données binaires sous forme de
                                 //   tableau d'octets en Java, ce qui correspond bien à la manière dont les données mediumblob sont gérées dans une base de données.
    private byte[] visualinfos;
    @Enumerated(EnumType.STRING)
    @Column(name = "access", nullable = false)
    private Access access;
    @Column(name = "time_limit")
    private int timelimit;
    @ManyToOne
    @JoinColumn(name = "ID_user", nullable = false)
    private Users user;
    @ManyToMany(mappedBy = "questions") // mappedBy indique que la relation est gérée de l'autre côté de la relation n-n par la collection questions ( donc dans l'entité Quizz.java)
    private Set<Quizz> quizzs = new HashSet<>();
    @ManyToMany
    @JoinTable( // Cette annotation est utilisée pour spécifier la table de jointure utilisée pour la relation many-to-many
            name = "techs_question", // Le nom de la table de jointure dans la base de données.
            joinColumns = @JoinColumn(name = "ID_question"), // Spécifie la colonne de jointure dans la table de jointure pour l'entité propriétaire de la relation
            inverseJoinColumns = @JoinColumn(name = "ID_tech")) // Spécifie la colonne de jointure dans la table de jointure pour l'entité non- propriétaire de la relation (ici Quizz)

    private Set<Tech> techs = new HashSet<>();  //c'est une COLLECTION DE techs. elle permet de gérer facilement les technos associées à chaque question. Le choix de Set plutôt que
                                               // List est généralement pour éviter les doublons, garantissant que la même tech ne peut pas être associée plusieurs fois à la même
                                              //  question.HashSet est une implémentation de l'interface Set qui utilise une table de hachage pour stocker les éléments. Elle ne permet
                                             //   pas de doublons, ce qui est idéal pour les relations many-to-many

    // Constructeur, getters et setters

    public Question()
    {
    }

    public Question(String content, String expectedanswer, Access access, Users user)
    {
        this.content = content;
        this.expectedanswer = expectedanswer;
        this.access = access;
        this.user = user;
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
