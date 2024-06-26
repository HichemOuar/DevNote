package com.example.DevNote.DTO;


import com.example.DevNote.model.Access;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateQuestionDTO {

    @NotBlank(message = "Veuillez renseigner un intitulé de question") // @NotBlank vérifie que la valeur n'est pas nulle et que la taille de la chaîne est supérieure à zéro
    @Size(max = 65535, message = "L'intitulé de la question est trop long") // 65535 est la taille maximale du champs, qui est de type TEXT en base de données
    private String content;

    @NotBlank(message = "Veuillez renseigner la réponse attendue")
    @Size(max = 65535, message = "L'intitulé de la question est trop long") // 65535 est la taille maximale du champs, qui est de type TEXT en base de données
    private String expectedanswer;

    private boolean access; // Ce champ capture l'état de la checkbox


    // Getters et setters:


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpectedanswer() {
        return expectedanswer;
    }

    public void setExpectedanswer(String expectedanswer) {
        this.expectedanswer = expectedanswer;
    }

    public boolean access() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    // Méthode pour obtenir l'Access enum basé sur la checkbox.
    public Access getAccess() {
        return access ? Access.publique : Access.privé; // ? indique une opération ternaire, qui est un raccourci pour une instruction if-else.:
        // if access est true, alors l'opération retournera la première valeur après le signe ?. Sinon, elle renverra la deuxième valeur.

    }

}
