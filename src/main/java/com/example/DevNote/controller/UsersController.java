package com.example.DevNote.controller;

import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.security.reCaptcha.CaptchaValidator;
import com.example.DevNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller

@RequestMapping("/users") // Définit l'URL de base pour toutes les requêtes gérées par ce contrôleur. Ainsi, toutes les méthodes de ce contrôleur commenceront par /api/users.
public class UsersController {

    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaValidator validator;

    @PostMapping("/register")  //  Déclare que cette méthode gère les requêtes POST à l'URL /api/users/register

    public String registerUser(@ModelAttribute @Validated UsersRegistrationDTO userdto, BindingResult bindingResult,
                                          @RequestParam("g-recaptcha-response") String captcha)

    // Lorsqu'un formulaire est soumis à un serveur, il envoie les données sous forme de paires clé-valeur. Sans @ModelAttribute, on devrait récupérer chaque valeur
    // individuellement à partir des paramètres de la requête. @ModelAttribute automatise ce processus et crée un objet userdto à partir des données envoyées par le client.
    // @Validated active la validation des données selon les annotations de validation dans UsersRegistrationDTO.i il y a des erreurs, elles seront contenues dans la variable
    // bindingResult.
    // @RequestParam("g-recaptcha-response") String captcha récupère la réponse du captcha envoyée depuis le formulaire: L'annotation @RequestParam est utilisée dans Spring
    // pour permettre à la méthode du contrôleur de récupérer des valeurs spécifiques envoyées par requête HTTP via le client (dans notre cas le formulaire) et de les utiliser
    // dans le traitement du serveur. g-recaptcha-response correspond au nom de l'input du captcha dans le formulaire. Lorsque qu'on intègre reCAPTCHA dans une page web, le
    // widget de reCAPTCHA génère automatiquement un champ caché avec le nom g-recaptcha-response dans le formulaire HTML. Ce champ est rempli automatiquement par reCAPTCHA
    // une fois que l'utilisateur a réussi le test CAPTCHA.


    {
        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            System.out.println("Erreurs de validation des entrées: "+(bindingResult.getAllErrors()));
            return "register";
        }

        if(!validator.isValidCaptcha(captcha)) { // Cette méthode appelle CaptchaValidator pour vérifier si la réponse du captcha est valide.
            System.out.println("Captcha invalide: "+(bindingResult.getAllErrors()));
            return "register";
        }

        try  // On ajoute un bloc try catch vérifier s'il n'y a pas d'autre type d'erreurs: Par exemple, pour la disponibilité de username/mail, ça ne se fait pas au niveau du DTO!
        {
            userService.createUser(userdto);
            System.out.println("Création du compte utilisateur réussie");
            return "home";
        }
        catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
        // exceptions qui descendent de cette classe.
        {
            System.out.println("Erreurs innatendues: "+(e.getMessage()));
            return "register";

        }
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}
