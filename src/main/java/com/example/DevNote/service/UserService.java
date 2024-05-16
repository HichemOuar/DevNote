package com.example.DevNote.service;
import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.model.Role;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Users createUserTestMapping(String username, String email, String password, Role role) {
        Users user = new Users(username, email, password, role);
        usersRepository.save(user);
        return user;
    }

    @Transactional
    public Users createUser(UsersRegistrationDTO dto) throws Exception  // NB: très important : Quand tu définis une exception dans une méthode, quand tu appelles cette méthode,
                                                                        // il faut utiliser un try catch ou un autre moyen de gérer les exceptions
    {
        if (usersRepository.existsByUsername(dto.getUsername()))
        {
            throw new Exception("Le nom d'utilisateur est déjà pris.");
        }
        if (usersRepository.existsByEmail(dto.getEmail()))
        {
            throw new Exception("L'adresse e-mail est déjà utilisée.");
        }

        Role defaultrole = Role.Apprenant;
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        Users user = new Users(dto.getUsername(), dto.getEmail(), hashedPassword, defaultrole);
        usersRepository.save(user);
        return user;
    }
}