package com.example.DevNote.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;



@Component /* Fait de cette classe un bean Spring, permettant à Spring de gérer son cycle de vie et de l'injecter là où elle est nécessaire*/
public class TokenProvider { // Elle va être utilisée pour générer des jetons JWT (JSON Web Tokens).

    @Value("${JWT_SECRET_KEY}") // La variable d'enrivonnement en question. pas besoin de la définir dans application.properties!
    private String secretkey;  //  Déclare une clé secrète qui sera utilisée pour signer le JWT. Donnée sensible, il ne faut donc pas l'afficher en dur! On doit utiliser
                                                   // une variable d'environnement




    public String generateToken(Authentication authentication) { // Chaque fois qu'un utilisateur se connecte avec succès, cette méthode est appelée et récupère un objet Authentication (qui contient les informations de l'utilisateur
                                                                //  authentifié) pour générer un jeton JWT qui lui est renvoyé. L'utilisateur doit ensuite inclure ce token dans les
                                                               //   en-têtes de ses requêtes subséquentes pour accéder aux ressources protégées. Le serveur valide ce token à chaque
                                                              //    requête pour s'assurer qu'il est toujours valide et que l'utilisateur a les droits d'accès nécessaires.
        return JWT.create()
                .withSubject(authentication.getName()) // Définit le sujet du token, généralement le nom d'utilisateur ou l'identifiant unique de l'utilisateur. authentication.getName()
                                                      //  récupère le nom d'utilisateur de l'objet Authentication.
                .withIssuedAt(new Date()) //   Enregistre la date et l'heure actuelles comme moment de l'émission du token.
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // Définit la date d'expiration du token à 24 heures après l'émission.
                .sign(Algorithm.HMAC512(secretkey.getBytes())); // Signe le token avec l'algorithme HMAC512 en utilisant la clé secrète définie. Cela garantit que le token ne peut pas
                                                               // être modifié sans avoir accès à la clé secrète.
    }
}