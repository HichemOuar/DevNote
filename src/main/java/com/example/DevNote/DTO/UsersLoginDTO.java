package com.example.DevNote.DTO;


import jakarta.validation.constraints.NotBlank;


public class UsersLoginDTO {



    @NotBlank(message = "Veuillez renseigner un nom d'utilisateur")
    private String username;

    @NotBlank(message = "Veuillez renseigner un mot de passe")
    private String password;



    public @NotBlank(message = "Veuillez renseigner un nom d'utilisateur") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Veuillez renseigner un nom d'utilisateur") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Veuillez renseigner un mot de passe") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Veuillez renseigner un mot de passe") String password) {
        this.password = password;
    }


}