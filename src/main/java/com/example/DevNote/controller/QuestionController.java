package com.example.DevNote.controller;


import com.example.DevNote.DTO.CreateQuestionDTO;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
import com.example.DevNote.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question") // Définit l'URL de base pour toutes les requêtes gérées par ce contrôleur. Ainsi, toutes les méthodes de ce contrôleur commenceront par /api/users.
public class QuestionController

{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UsersRepository usersRepository;


    @PostMapping("/create")
    public ResponseEntity<?> createQuestion(@ModelAttribute @Validated CreateQuestionDTO questiondto, BindingResult bindingResult)

    {
        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            // Si des erreurs de validation sont détectées, retourner une réponse avec ces erreurs
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try  // On ajoute un bloc try catch vérifier s'il n'y a pas d'autre type d'erreurs: Par exemple, pour la disponibilité de username/mail, ça ne se fait pas au niveau du DTO!
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Récupère le nom d'utilisateur de l'utilisateur connecté
            Users user = usersRepository.getUserByUsername(username); // Récupère l'entité utilisateur en utilisant le nom d'utilisateur
            questionService.createQuestion(questiondto,user);
            return ResponseEntity.ok("Création de la question réussie"); // Si aucune erreur n'est détectée, passer au service pour créer l'utilisateur
        }
        catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
        // exceptions qui descendent de cette classe.
        {
            return ResponseEntity.badRequest().body(e.getMessage()); // e.getMessage() renvoie le message d'erreur associé à l'exception capturée. Ce message est généralement fourni
            // par le constructeur de l'exception ou par une surcharge de cette exception
        }
    }


}