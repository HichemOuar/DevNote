package com.example.ProjetCDA.repository;

import com.example.ProjetCDA.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}