package com.example.DevNote.controller;

import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.security.reCaptcha.CaptchaValidator;
import com.example.DevNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

@RequestMapping("/users") // Définit l'URL de base pour toutes les requêtes gérées par ce contrôleur. Ainsi, toutes les méthodes de ce contrôleur commenceront par /api/users.
public class UsersController {

    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaValidator validator;

    @PostMapping("/register")  //  Déclare que cette méthode gère les requêtes POST à l'URL /api/users/register
    @ResponseBody
    public ResponseEntity<?> registerUser(@ModelAttribute @Validated UsersRegistrationDTO userdto, BindingResult bindingResult,
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
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return ResponseEntity.badRequest().body(errors);
        }

        if (!validator.isValidCaptcha(captcha)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Captcha Invalide");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            userService.createUser(userdto);
            Map<String, String> success = new HashMap<>();
            success.put("message", "User registered successfully");
            return ResponseEntity.ok().body(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
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
