package com.example.DevNote.controller;

import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController // Cette annotation indique que la classe est un contrôleur REST.REST est une manière standard de structurer les communications entre un client (comme un navigateur web ou
// une application mobile) et un serveur. Un service web RESTful permet au client de récupérer ou modifier des informations sur le serveur à travers des requêtes HTTP standards
// (comme GET pour récupérer des informations ou POST pour envoyer des informations).
// Le @RestController simplifie le code nécessaire pour envoyer les données directement à qui les demande (par exemple, le navigateur ou une application mobile.
//Contrairement à certains autres contrôleurs qui renvoient des pages web complètes (avec HTML, CSS, etc.), un @RestController renvoie généralement juste des données.
// Ces données peuvent être sous forme de texte simple, JSON, ou XML. En renvoyant des données au lieu de HTML, les @RestControllers sont très utiles quand on veut que le backend
// soit séparé du frontend. Cela permet aux développeurs de frontend de construire l'interface utilisateur comme ils le souhaitent, en utilisant les données reçues via les @RestController
// (et aussi certains problèmes de sécurité)

@RequestMapping("/api/users") // Définit l'URL de base pour toutes les requêtes gérées par ce contrôleur. Ainsi, toutes les méthodes de ce contrôleur commenceront par /api/users.
public class UsersController {

    @Autowired
    private UserService userService;
    

    @PostMapping("/register")  //  Déclare que cette méthode gère les requêtes POST à l'URL /api/users/register

    public ResponseEntity<?> registerUser(@ModelAttribute @Validated UsersRegistrationDTO userdto, BindingResult bindingResult) //ResponseEntity est un type qui encapsule des informations de
    // réponse HTTP, y compris le statut, les en-têtes et le corps. Le caractère générique <?> indique que cette méthode peut renvoyer une réponse de n'importe quel type.
    // @RequestBody aide la méthode du contrôleur registerUser à comprendre que les données de ce formulaire ou de cette application doivent être prises et transformées en un objet Java.
    // (Prends les données envoyées avec cette requête HTTP et utilise-les pour remplir l'objet spécifié ici).@Validated active la validation des données selon les annotations de
    // validation dans UsersRegistrationDTO.Si il y a des erreurs, elles seront contenues dans la variable bindingResult

    {
        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            // Si des erreurs de validation sont détectées, retourner une réponse avec ces erreurs
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try  // On ajoute un bloc try catch vérifier s'il n'y a pas d'autre type d'erreurs: Par exemple, pour la disponibilité de username/mail, ça ne se fait pas au niveau du DTO!
        {
            userService.createUser(userdto);
            return ResponseEntity.ok("Création du compte utilisateur réussie"); // Si aucune erreur n'est détectée, passer au service pour créer l'utilisateur
        }
        catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
        // exceptions qui descendent de cette classe.
        {
            return ResponseEntity.badRequest().body(e.getMessage()); // e.getMessage() renvoie le message d'erreur associé à l'exception capturée. Ce message est généralement fourni
                                                                    // par le constructeur de l'exception ou par une surcharge de cette exception
        }
    }

}
