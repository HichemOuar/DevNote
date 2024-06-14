package com.example.DevNote.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


@EnableWebSecurity
@Configuration // Indique que la classe contient des méthodes @Bean et peut être utilisée par le conteneur Spring pour générer des définitions de bean et des demandes de service
public class SecurityConfig  {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() { // DaoAuthenticationProvider est un fournisseur d'authentification spécifique utilisé par Spring Security. Il sert de
        // pont entre les entités utilisateur (les données que l'on stocke sur les utilisateurs) et le mécanisme d'authentification de Spring Security.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); //  utilise un UserDetailsService pour charger les détails d'un utilisateur à partir de la base de données
        // lorsqu'une tentative de connexion est faite.
        authProvider.setPasswordEncoder(passwordEncoder()); //  Une fois que les détails de l'utilisateur sont chargés, le DaoAuthenticationProvider utilise un PasswordEncoder
        // pour vérifier le mot de passe fourni lors de la connexion. Il compare le mot de passe crypté stocké en BDD avec celui qui est soumis après l'avoir également crypté

        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Le SecurityFilterChain est essentiellement une chaîne de filtres que Spring Security utilise pour
        // gérer la sécurité des requêtes entrantes dans votre application. Chaque filtre a une responsabilité spécifique, comme la gestion des sessions, la redirection des
        // utilisateurs non authentifiés vers la page de connexion, etc.
        http
                .authorizeHttpRequests(auth -> auth // Configure quelles URL nécessitent une authentification et lesquelles sont accessibles publiquement.
                        .requestMatchers("/vue/users/register","/api/users/register","/logout").permitAll()
                        .requestMatchers("/vue/users/home","vue/questionboard","vue/searchquestion","vue/createquestion").hasAnyAuthority("Admin","Apprenant")
                        .requestMatchers("/bootstrap/**", "/css/**", "/js/**", "/fonts/**").permitAll() // Autoriser l'accès aux ressources statiques
                        .anyRequest().authenticated()) // toutes les autres requêtes nécessitent une authentification
                .formLogin(login -> login
                        .loginPage("/vue/users/login")
                        .defaultSuccessUrl("/vue/users/home", true)  // Redirige vers la page home après une connexion réussie
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/vue/users/login")  // Rediriger après la déconnexion
                        .deleteCookies("JSESSIONID")  // Supprimer le cookie de session
                        .invalidateHttpSession(true))  // Invalider la session HTTP
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/**"));
                // Désactive CSRF uniquement pour les chemins spécifiés
        ;
        return http.build();
    }

}