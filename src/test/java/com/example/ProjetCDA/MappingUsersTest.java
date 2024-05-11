package com.example.ProjetCDA;

import com.example.ProjetCDA.model.Users;
import com.example.ProjetCDA.model.Role;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest // Cette annotation configure une base de données en mémoire, scanne les entités et configure Spring Data JPA
public class MappingUsersTest
{

	@Autowired // permet à Spring d'injecter automatiquement les dépendances nécessaires. Ici, il est utilisé pour injecter une instance de TestEntityManager, un composant de Spring
			  // Boot utilisé pour gérer les entités dans les tests.

	private TestEntityManager entityManager;  // Cette variable est une instance créée de la classe TestEntityManager, utilisée pour gérer les opérations sur la base de données comme
											 // l'insertion ou la recherche d'entités.

	@Test  // Cette annotation indique que la méthode qui suit est une méthode de test. Cela signifie que lors de l'exécution des tests, cette méthode sera exécutée automatiquement
		  // par le framework de test.

	public void testInsertionUserOK()
	{
		Users user = new Users();
		user.setUsername("username");
		user.setPassword("password");
		user.setEmail("email@example.com");
		user.setRole(Role.Apprenant);
		entityManager.persist(user); //La méthode persist est utilisée pour insérer une entité dans la base de données. Elle rend l'instance gérée, ce qui signifie que
		 							//toute modification apportée à cette instance dans le contexte de la transaction courante sera synchronisée avec la base de données
		entityManager.flush();	   //flush est utilisé pour forcer l'envoi des modifications en attente à la base de données. Cela est utile pour garantir que toutes les opérations en
								  // attente sont appliquées avant de continuer.

		Users userbdd = entityManager.find(Users.class, user.getId()); //find est utilisé pour récupérer l'entité en BASE DE DONNEES que l'on a créée à partir de l'entité java. En
																      // paramètres, on passe le type d'identité java ( Users.class) et l'identification de l'identité (user.getId())

		assertThat(userbdd).isEqualTo(user);  // on compare l'objet en base de données (userbdd) avec l'objet créé dans la méthode de test ( user)
	}
	@Test
	public void testInsertionUserKONoPassword()
	{
		Users user = new Users();
		user.setUsername("username");
		user.setEmail("email@example.com");
		user.setRole(Role.Apprenant);

		assertThrows(PersistenceException.class, () -> // Cette instruction est utilisée pour vérifier (avec un assert) qu'une exception spécifique est levée lors de l'exécution du code à l'intérieur
													  // du lambda. L'exception en question est ici PersistenceException
		{
			entityManager.persist(user);
			entityManager.flush();
		});
	}

	@Test
	public void testInsertionUserKONoUsername()
	{
		Users user = new Users();
		user.setEmail("email@example.com");
		user.setRole(Role.Apprenant);
		user.setPassword("password");

		assertThrows(PersistenceException.class, () ->
		{
			entityManager.persist(user);
			entityManager.flush();
		});
	}

	@Test
	public void testInsertionUserKONoEmail()
	{
		Users user = new Users();
		user.setUsername("username");
		user.setRole(Role.Apprenant);
		user.setPassword("password");

		assertThrows(PersistenceException.class, () ->
		{
			entityManager.persist(user);
			entityManager.flush();
		});
	}

	@Test
	public void testInsertionUserKONoRole()
	{
		Users user = new Users();
		user.setUsername("username");
		user.setPassword("password");
		user.setEmail("email@example.com");

		assertThrows(PersistenceException.class, () ->
		{
			entityManager.persist(user);
			entityManager.flush();
		});
	}

}