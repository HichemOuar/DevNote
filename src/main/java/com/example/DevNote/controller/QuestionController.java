package com.example.DevNote.controller;


import com.example.DevNote.DTO.CreateQuestionDTO;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.QuestionRepository;
import com.example.DevNote.repository.UsersRepository;
import com.example.DevNote.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/question") // Définit l'URL de base pour toutes les requêtes gérées par ce contrôleur. Ainsi, toutes les méthodes de ce contrôleur commenceront par /api/users.
public class QuestionController

{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @PostMapping("/create")
    public String createQuestion(@ModelAttribute @Validated CreateQuestionDTO questiondto, BindingResult bindingResult)

    {
        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            System.out.println("Erreurs de validation des entrées: "+(bindingResult.getAllErrors()));
            return "createquestion";
            // Si des erreurs de validation sont détectées, retourner une réponse avec ces erreurs

        }

        try  // On ajoute un bloc try catch vérifier s'il n'y a pas d'autre type d'erreurs: Par exemple, pour la disponibilité de username/mail, ça ne se fait pas au niveau du DTO!
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Récupère le nom d'utilisateur de l'utilisateur connecté
            Users user = usersRepository.getUserByUsername(username); // Récupère l'entité utilisateur en utilisant le nom d'utilisateur
            questionService.createQuestion(questiondto,user);
            System.out.println("Création de la question réussie");
            return "questionboard";
        }
        catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
        // exceptions qui descendent de cette classe.
        {
            System.out.println("Erreurs innatendues: "+(e.getMessage())); // e.getMessage() renvoie le message d'erreur associé à l'exception capturée. Ce message est généralement fourni
            // par le constructeur de l'exception ou par une surcharge de cette exception
            return "createquestion";
        }
    }



    @GetMapping("/search")
    public String searchQuestions(Model model) { //  Model est un objet Spring utilisé pour ajouter des attributs qui peuvent être utilisés par la vue.
        // Ici, il est passé en paramètre à la méthode pour permettre l'ajout d'attributs qui seront ensuite accessibles dans le fichier template HTML.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = usersRepository.getUserByUsername(username);
        List<Question> questions = questionService.getQuestionsForUserAndPublic(user);
        model.addAttribute("questions", questions); // on ajoute la liste des questions au modèle, ce qui les rend accessibles dans la vue searchquestion côté client.
        model.addAttribute("currentUserId", user.getId()); // On récupère l'ID de l'utilisateur pour afficher les boutons modifier/supprimer uniquement a côté
                                                                      // des questions qu'il a créées.

        return "searchquestion"; // La méthode renvoie le nom de la vue searchquestion
    }


    @GetMapping("/answer")
    public String answerQuestion(@RequestParam("questionId") Integer questionId,Model model) { // @RequestParam est utilisée pour extraire la valeur de l'ID de la question
                                                                                              // à partir du formulaire dans searchquestion.html
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isPresent()) {
            model.addAttribute("question", question.get()); // question.get() est utilisé pour obtenir la valeur réelle de l'objet Optional<Question>. Optional est une classe en
            // Java qui sert à encapsuler un type de donnée qui peut être soit null, soit contenir une valeur. question.get() : Cette méthode retourne l'objet Question contenu dans
            // l'Optional s'il est présent. Si l'Optional est vide (c'est-à-dire qu'il ne contient pas de valeur et représente donc un cas où la question n'a pas été trouvée), alors cette
            // méthode lancerait une NoSuchElementException si elle est appelée sans vérifier si la valeur est présente. C'est pourquoi elle est généralement précédée par
            // question.isPresent() pour vérifier si l'objet contient une valeur.
            return "answerquestion";
        } else {
            System.out.println("La question n'a pas été trouvée dans la base de données");
            return "searchquestion";
        }
    }



    @GetMapping("/questionboard")
    public String questionboard() {
        return "questionboard";
    }


    @GetMapping("/createquestion")
    public String createquestion() {
        return "createquestion";
    }


}
