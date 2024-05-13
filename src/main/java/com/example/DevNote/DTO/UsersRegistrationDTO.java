package com.example.DevNote.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsersRegistrationDTO {
    @NotBlank(message = "Veuillez renseigner un nom d'utilisateur")
    @Size(max = 15, message = "Le nom d'utilisateur ne peut pas dépasser 15 caractères")
    private String username;

    @NotBlank(message = "Veuillez renseigner un email")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Veuillez renseigner un mot de passe")
    @Size(min = 8, max = 25, message = "Veuillez renseigner un mot de passe")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\\W]).*$", message = "Le mot de passe doit contenir des lettres, des chiffres et des caractères spéciaux")
    private String password;


}