package com.example.DevNote.service;
import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.model.Role;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService  {


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

        // Protection contre les attaques XSS: Une attaque XSS (Cross-Site Scripting) permet à un attaquant d'injecter des scripts malveillants dans des pages web vues par
        // des utilisateurs. Ces scripts peuvent voler des informations sensibles, comme des cookies de session, rediriger des utilisateurs vers des sites malveillants ou
        // manipuler le contenu affiché sur la page.
        // Supposons qu'un site web permet aux utilisateurs de publier des commentaires. Si l'application web ne valide pas correctement les entrées utilisateur, un
        // attaquant pourrait soumettre un commentaire avec un script malveillant. Si ce commentaire est affiché directement sur la page sans être nettoyé, le script
        // s'exécutera dans le navigateur de tout utilisateur qui visualise la page contenant ce commentaire.
        // différence entre injection sql et attaque xss: Les attaques XSS visent à exécuter des scripts malveillants dans le navigateur de l'utilisateur, tandis que les
        // injections SQL exploitent les failles dans les requêtes SQL pour manipuler les bases de données.
        // Pour lutter contre les attaques XSS, il faut nettoyer les entrées utilisateur. Ci-dessous, on va utiliser Jsoup qui est une bibliothèque Java qui permet
        // d'enlever tout code malveillant potentiel des entrées utilisateur.

        String sanitizedUsername = Jsoup.clean(dto.getUsername(), Safelist.none());
        String sanitizedEmail = Jsoup.clean(dto.getEmail(), Safelist.none());

        // la méthode Jsoup.clean(String html, Safelist whitelist) nettoie le contenu HTML passé en premier argument (ici, le nom d'utilisateur et l'email).
        // et le second argument, Safelist.none(), est une liste blanche qui spécifie quels éléments HTML et attributs sont autorisés. Safelist.none() signifie qu'aucun
        // élément HTML n'est autorisé, ce qui élimine tout le HTML de l'entrée.

        // Il n'est pas utile de sanitize le mot de passe puisque : 1- il va être haché et n'est jamais stocké en clair; 2- il est souvent composé de caractères spéciaux
        // donc l'altérer pourrait causer des problèmes.

        Role defaultrole = Role.Apprenant;
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        Users user = new Users(sanitizedUsername, sanitizedEmail, hashedPassword, defaultrole);
        usersRepository.save(user);
        return user;
    }


}
