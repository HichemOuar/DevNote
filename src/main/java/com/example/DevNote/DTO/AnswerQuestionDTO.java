package com.example.DevNote.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnswerQuestionDTO {

    @NotBlank(message = "Veuillez renseigner une réponse") // @NotBlank vérifie que la valeur n'est pas nulle et que la taille de la chaîne est supérieure à zéro
    @Size(max = 65535, message = "La réponse est trop longue") // 65535 est la taille maximale du champs, qui est de type TEXT en base de données
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
