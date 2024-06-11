package com.example.DevNote.security;

import com.example.DevNote.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class MyUserDetails implements UserDetails { //  UserDetails est une interface centrale dans Spring Security. Elle est utilisée pour encapsuler les informations d'un utilisateur,
    // que Spring Security utilisera ensuite pour réaliser l'authentification et d'autres vérifications de sécurité. Cette interface définit plusieurs méthodes essentielles que
    // Spring Security utilise pour contrôler l'accès, telles que getUsername(), getPassword(), et getAuthorities(), ainsi que des 	méthodes pour vérifier si le compte est toujours
    // actif (non verrouillé etc..). On a besoin d'override les méthodes de UserDetails pour pouvoir utiliser hasAnyAuthority de Spring security ( car le j'ai défini le role comme une
    // enum et non un string)

    private Users user;

    public MyUserDetails(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name()); // La méthode name() permet de convertir le role ( qui est une enum chez moi) en string, qui est
                                                                                             // le type de paramètre attendu par la méthode SimpleGrantedAuthority de Spring security
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}