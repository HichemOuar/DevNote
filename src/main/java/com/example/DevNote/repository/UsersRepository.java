package com.example.DevNote.repository;

import com.example.DevNote.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {  //UsersRepository hérite de l'interface JpaRepository. Cette dernière prend deux paramètres génériques : le type de
                                                                      // l'entité (Users) et le type de la clé primaire (Long). En héritant de JpaRepository, UsersRepository bénéficie
                                                                     // automatiquement de plusieurs méthodes utiles pour la manipulation des données (comme save(), delete(), findById(),
                                                                    // findAll(), etc.).

}