package com.example.DevNote.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indique que la classe contient des méthodes @Bean et peut être utilisée par le conteneur Spring pour générer des définitions de bean et des demandes de service
@EnableWebSecurity // Active la sécurité web dans le projet Spring. Il charge la configuration de sécurité Web par défaut de Spring Security.

public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  // SecurityFilterChain est l'nterface utilisée pour créer une chaîne de filtres de sécurité dans Spring
                                                                               // Security
    {
        http
                .csrf(AbstractHttpConfigurer::disable) // http.csrf(AbstractHttpConfigurer::disable): Désactive la protection CSRF (Cross-Site Request Forgery), qui est une attaque qui
                // force un utilisateur final à exécuter des actions non souhaitées sur une application web dans laquelle il est actuellement authentifié.Cela n'est pas nécessaire car on va
                // utiliser des API REST ( qui sont, par principe, sans état, c'est à dire que chaque requête d'un client vers le serveur doit contenir toutes les informations
                // nécessaires pour comprendre et traiter la requête. En d'autres termes, le serveur ne doit pas garder de trace de l'état précédent du client),
                // l'authentification va être gérée à l'aide de tokens comme (JSON Web Tokens), qui ne nécessitent pas de protection CSRF.

                .authorizeHttpRequests(auth -> auth // http.authorizeHttpRequests(): Permet de configurer l'autorisation des requêtes HTTP
                        .requestMatchers("/login", "/register").permitAll() // auth.requestMatchers("/login", "/register").permitAll() permet à tout le monde d'accéder aux pages
                                                                                    // de login et d'enregistrement sans être authentifié.

                        .anyRequest().authenticated()) // auth.anyRequest().authenticated(): exige que toutes les autres demandes soient authentifiées.
                .formLogin(form -> form // http.formLogin(): Permet la configuration de l'authentification basée sur les formulaires
                        .loginPage("/login")  // form.loginPage("/login"): Définit la page de connexion personnalisée.
                        .defaultSuccessUrl("/home", true)) // form.defaultSuccessUrl("/home", true): Redirige vers la page spécifiée après une connexion réussie.
                .logout(logout -> logout // http.logout(): Permet de configurer la déconnexion
                        .logoutSuccessUrl("/login")); // logout.logoutSuccessUrl("/login"): Spécifie la page de redirection après déconnexion.

        return http.build(); // Sauvegarde en quelques sortes les configurations de sécurité
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Dans Spring Security, l'AuthenticationManager est une interface centrale qui gère le processus d'authentification. AuthenticationConfiguration est un paramètre injecté par
        // Spring qui permet d'accéder à la configuration d'authentification définie dans l'application
        return authenticationConfiguration.getAuthenticationManager(); // Cette ligne retourne un AuthenticationManager configuré.  authenticationConfiguration.getAuthenticationManager()
        // fait partie de Spring Security 5 et offre une manière simplifiée d'obtenir un AuthenticationManager préconfiguré avec les réglages par défaut ou personnalisés définis ailleurs dans ton application.
        //Cela évite de devoir configurer manuellement un AuthenticationManager, en tirant parti de la configuration automatique de Spring Security.
    }
}
