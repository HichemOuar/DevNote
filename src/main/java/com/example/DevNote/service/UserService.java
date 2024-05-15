package com.example.DevNote.service;
import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.model.Role;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public Users createUserTestMapping(String username, String email, String password, Role role) {
        Users user = new Users(username, email, password, role);
        usersRepository.save(user);
        return user;
    }

    public Users createUser(UsersRegistrationDTO dto) {
        Role defaultrole = Role.Apprenant;
        Users user = new Users(dto.getUsername(), dto.getEmail(), dto.getPassword(), defaultrole);
        usersRepository.save(user);
        return user;
    }
}