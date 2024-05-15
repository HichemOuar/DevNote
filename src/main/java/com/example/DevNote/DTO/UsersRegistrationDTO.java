package com.example.DevNote.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsersRegistrationDTO {

    @NotBlank(message = "Veuillez renseigner un nom d'utilisateur") //La condition "le champs ne doit pas être nul' est implicitement gérée par l'annotation elle-même, ce qui est une
                                                                   // des grandes forces d'utiliser des annotations prédéfinies pour la validation dans Spring Boot. Vaut aussi pour
                                                                  //  les autres annotations
    @Size(max = 15, message = "Le nom d'utilisateur ne peut pas dépasser 15 caractères")
    private String username;
    @NotBlank(message = "Veuillez renseigner un email")
    @Email(message = "Format d'email invalide")
    private String email;
    @NotBlank(message = "Veuillez renseigner un mot de passe")
    @Size(min = 8, max = 25, message = "Veuillez renseigner un mot de passe")

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\\W]).*$", message = "Le mot de passe doit contenir des lettres, des chiffres et des caractères spéciaux")
    private String password;
    @NotBlank(message = "Le captcha est requis")  // On s'occupera du captcha plus tard, notamment quand on aura commencé le front-end
    private String captchaResponse;

    // Getters et Setters:

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaResponse() {
        return captchaResponse;
    }

    public void setCaptchaResponse(String captchaResponse) {
        this.captchaResponse = captchaResponse;
    }

}