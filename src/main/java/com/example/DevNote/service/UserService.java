package com.example.DevNote.service;
import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.model.Role;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService  implements UserDetailsService { // Implémentation de l'interface UserDetailsService requise par Spring Security pour charger les détails d'un utilisateur par son nom
                                                         // d'utilisateur.



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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Implémentation REQUISE par UserDetailsService. Cette méthode est utilisée par Spring Security lors des tentatives de
        // connexion pour récupérer un utilisateur par son nom et le convertir en un format que Spring Security peut utiliser.. Si l'utilisateur n'est pas trouvé, une exception est levée.
        // Les autorités sont également définies ici et déterminent les rôles et les permissions de l'utilisateur dans l'application.
        // En gros, ce que cette méthode fait, c'est qu'elle va permettre la comparaison entre les valeurs entrées dans le formulaire et les valeeurs réelles en BDD.

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // Création de la liste des autorités
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("APPRENANT", "ADMIN");

        // Utiliser la classe User de Spring Security pour créer un UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

}
