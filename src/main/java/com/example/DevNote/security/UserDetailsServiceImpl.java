package com.example.DevNote.security;

import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService  // L'interface UserDetailsService, qui joue un rôle crucial dans la récupération des informations sur
// l'utilisateur de la base de données. La méthode principale loadUserByUsername(String username) doit être implémentée pour rechercher un utilisateur par son nom
// d'utilisateur et retourner un objet UserDetails.
{
    @Autowired
    private UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable");
        }

        return new MyUserDetails(user);
    }

}