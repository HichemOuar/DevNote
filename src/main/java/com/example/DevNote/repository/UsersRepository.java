package com.example.DevNote.repository;

import com.example.DevNote.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {  //UsersRepository hérite de l'interface JpaRepository. Cette dernière prend deux paramètres génériques : le type de
    // l'entité (Users) et le type de la clé primaire (Integer). En héritant de JpaRepository, UsersRepository bénéficie
    // automatiquement de plusieurs méthodes utiles pour la manipulation des données (comme save(), delete(), findById(),
    // findAll(), etc.).

    // Méthode pour vérifier l'existence d'un utilisateur par son username
    boolean existsByUsername(String username);

    // Méthode pour vérifier l'existence d'un utilisateur par son email
    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username); // Le optionnal permet d'éviter les erreurs de type NullPointerException si l'utilisateur n'est pas trouvé

}