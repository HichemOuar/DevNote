package com.example.DevNote.security.reCaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class CaptchaValidator {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Environment env;  // permet d'accéder aux variables d'environnement définies dans application.properties

    public boolean isValidCaptcha(String captcha) {

        String recaptchaSecretKey = env.getProperty("recaptcha.secret"); // récupère la clé secrète définie en variable d'environnement
        String url = "https://www.google.com/recaptcha/api/siteverify"+"?secret=" + recaptchaSecretKey + "&response=" + captcha;
        CaptchaResponse resp = restTemplate.postForObject(url, null, CaptchaResponse.class); // On utilise RestTemplate pour envoyer une requête HTTP POST à l'API de
        // Google. RestTemplate prend en charge la construction de la requête, l'envoi de la requête au serveur de Google, et la gestion de la connexion HTTP sous-jacente.
        // la réponse JSON est ensuite désérialisée (convertie) en un objet Java CaptchaResponse.
        // Dans notre cas, on utilise la méthode postForObject() de RestTemplate qui prend en paramètres 3 arguments: URL qui est l'adresse du serveur à laquelle la requête
        // doit être envoyée; Request Body qui est le corps de la requête. Dans notre cas, il est null parce que tous les paramètres nécessaires pour la requête sont inclus
        // dans l'URL elle-même (comme le secret de reCAPTCHA et la réponse de l'utilisateur); et enfin Response Class qui indique quel type d'objet il doit attendre en réponse du
        // serveur et comment il doit désérialiser la réponse JSON obtenue du serveur. RestTemplate utilise des bibliothèques de mapping JSON
        // pour convertir la réponse JSON en un objet CaptchaResponse.
        return resp.isSuccess();
    }

}