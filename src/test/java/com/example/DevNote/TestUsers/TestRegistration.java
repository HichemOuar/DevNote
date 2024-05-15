package com.example.DevNote.TestUsers;

import com.example.DevNote.model.Users;
import com.example.DevNote.model.Role;
import com.example.DevNote.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import com.example.DevNote.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

public class TestRegistration
{

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserService userService;

    @Test
    public void testCRUDUserOK()
    {
        Users user = userService.createUserTestMapping("utilisateur", "mail@example.com", "password", Role.Apprenant);
        assertThat(usersRepository.findById(user.getId())).isPresent();
        user.setEmail("newmail@example.com");
        usersRepository.save(user);
        assertThat(usersRepository.findById(user.getId())).isPresent().hasValueSatisfying(u -> assertThat(u.getEmail()).isEqualTo("newmail@example.com"));
        usersRepository.delete(user);
        assertThat(usersRepository.findById(user.getId())).isNotPresent();
    }
    @Test
    public void testInsertionUserKONoPassword()
    {
        Users user = new Users();
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setRole(Role.Apprenant);
        assertThrows(DataIntegrityViolationException.class, () -> usersRepository.saveAndFlush(user));
    }

    @Test
    public void testInsertionUserKONoUsername()
    {
        Users user = new Users();
        user.setEmail("email@example.com");
        user.setRole(Role.Apprenant);
        user.setPassword("password");
        assertThrows(DataIntegrityViolationException.class, () -> usersRepository.saveAndFlush(user)); // assertThrows vérifie qu'une exception spécifique est levée lors de l'exécution
        // du bloc de code donné, ce qui est attendu quand une contrainte d'intégrité est violée.
    }

    @Test
    public void testInsertionUserKONoEmail()
    {
        Users user = new Users();
        user.setUsername("username");
        user.setRole(Role.Apprenant);
        user.setPassword("password");
        assertThrows(DataIntegrityViolationException.class, () -> usersRepository.saveAndFlush(user));
    }

    @Test
    public void testInsertionUserKONoRole()
    {
        Users user = new Users();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@example.com");
        assertThrows(DataIntegrityViolationException.class, () -> usersRepository.saveAndFlush(user));
    }

}