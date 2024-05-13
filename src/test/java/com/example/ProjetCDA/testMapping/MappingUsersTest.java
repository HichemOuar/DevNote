package com.example.ProjetCDA.testMapping;

import com.example.ProjetCDA.model.Users;
import com.example.ProjetCDA.model.Role;
import com.example.ProjetCDA.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import com.example.ProjetCDA.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
// Cette annotation configure une base de données en mémoire, scanne les entités et configure Spring Data JPA
public class MappingUsersTest
{

	@Autowired // permet à Spring d'injecter automatiquement les dépendances nécessaires. Ici, il est utilisé pour injecter une instance de TestEntityManager, un composant de Spring
			  // Boot utilisé pour gérer les entités dans les tests.

	private UsersRepository usersRepository;
	@Autowired
	private UserService userService;

	@Test  // Cette annotation indique que la méthode qui suit est une méthode de test. Cela signifie que lors de l'exécution des tests, cette méthode sera exécutée automatiquement
		  // par le framework de test.
	public void testCRUDUserOK()
	{
		Users user = userService.createUser("utilisateur", "mail@example.com", "password", Role.Apprenant);
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
		assertThrows(DataIntegrityViolationException.class, () -> usersRepository.saveAndFlush(user));
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