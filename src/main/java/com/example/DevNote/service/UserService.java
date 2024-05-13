package com.example.DevNote.service;
import com.example.DevNote.model.Role;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public Users createUser(String username, String email, String password, Role role) {
        Users user = new Users(username, email, password, role);
        usersRepository.save(user);
        return user;
    }
}