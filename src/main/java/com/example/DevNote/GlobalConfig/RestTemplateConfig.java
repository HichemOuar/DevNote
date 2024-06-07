package com.example.DevNote.GlobalConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() { //  RestTemplate est une classe dans Spring Framework utilisée pour réaliser des appels HTTP. Elle simplifie la communication avec les
        // services web, rendant l'envoi de requêtes et la réception de réponses HTTP plus accessibles.C'est un client HTTP qui aide à intégrer l'application avec des
        // services RESTful externes, comme l'API de vérification de reCAPTCHA de Google dans notre cas.
        //La méthode restTemplate() {n'est pas là pour créer la classe RestTemplate elle-même, mais pour enregistrer une instance de cette classe comme un bean géré par
        // Spring.En déclarant RestTemplate comme un bean, on peut injecter RestTemplate partout où on en a besoin dans l'application via l'injection de dépendances
        //(@autowired).
        return new RestTemplate();
    }
}
