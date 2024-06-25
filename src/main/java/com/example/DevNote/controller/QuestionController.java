package com.example.DevNote.controller;


import com.example.DevNote.DTO.AnswerQuestionDTO;
import com.example.DevNote.DTO.CreateQuestionDTO;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.QuestionRepository;
import com.example.DevNote.repository.UsersRepository;
import com.example.DevNote.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.*;

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
    @ResponseBody
    public ResponseEntity<?> createQuestion(@ModelAttribute @Validated CreateQuestionDTO questiondto, BindingResult bindingResult)

    {
        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            return ResponseEntity.badRequest().body("Erreurs de validation des entrées: " + bindingResult.getAllErrors());
            // Si des erreurs de validation sont détectées, retourner une réponse avec ces erreurs

        }

        try  // On ajoute un bloc try catch vérifier s'il n'y a pas d'autre type d'erreurs: Par exemple, pour la disponibilité de username/mail, ça ne se fait pas au niveau du DTO!
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Récupère le nom d'utilisateur de l'utilisateur connecté
            Users user = usersRepository.getUserByUsername(username); // Récupère l'entité utilisateur en utilisant le nom d'utilisateur
            questionService.createQuestion(questiondto,user);
            return ResponseEntity.ok().body("Création de la question réussie");

        }
        catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
        // exceptions qui descendent de cette classe.
        {
            System.out.println("Erreurs innatendues: "+(e.getMessage())); // e.getMessage() renvoie le message d'erreur associé à l'exception capturée. Ce message est généralement fourni
            // par le constructeur de l'exception ou par une surcharge de cette exception
            return ResponseEntity.status(500).body("Erreurs inattendues: " + e.getMessage());
        }
    }



    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteQuestion(@RequestParam("questionId") Integer questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = usersRepository.getUserByUsername(username);
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        Map<String, String> response = new HashMap<>();
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (user.getId() == question.getUser().getId()) {
                questionRepository.deleteById(questionId);
                response.put("message", "Suppression de la question réussie");
                response.put("redirectUrl", "/question/search");
                return ResponseEntity.ok().body(response);
            } else {
                response.put("message", "Vous n'êtes pas autorisé à supprimer cette question");
                return ResponseEntity.status(403).body(response);
            }
        } else {
            response.put("message", "La question n'a pas été trouvée dans la base de données");
            return ResponseEntity.status(404).body(response);
        }
    }


    @GetMapping("/update") // accès a la vue permettant de répondre à une question donnée
    public String updateQuestion(@RequestParam("questionId") Integer questionId,Model model) { // @RequestParam est utilisée pour extraire la valeur de l'ID de la question
        // à partir du formulaire dans searchquestion.html
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isPresent()) {
            model.addAttribute("question", question.get()); // question.get() est utilisé pour obtenir la valeur réelle de l'objet Optional<Question>. Optional est une classe en
            // Java qui sert à encapsuler un type de donnée qui peut être soit null, soit contenir une valeur. question.get() : Cette méthode retourne l'objet Question contenu dans
            // l'Optional s'il est présent. Si l'Optional est vide (c'est-à-dire qu'il ne contient pas de valeur et représente donc un cas où la question n'a pas été trouvée), alors cette
            // méthode lancerait une NoSuchElementException si elle est appelée sans vérifier si la valeur est présente. C'est pourquoi elle est généralement précédée par
            // question.isPresent() pour vérifier si l'objet contient une valeur.
            return "updatequestion";
        } else {
            System.out.println("La question n'a pas été trouvée dans la base de données");
            return "searchquestion";
        }
    }


    @PostMapping("/update/submit")
    @ResponseBody
    public ResponseEntity<?> updateQuestionSubmit(@ModelAttribute @Validated CreateQuestionDTO updatedto, @RequestParam("questionId") Integer questionId,
                                                  BindingResult bindingResult) {

        Map<String, String> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            response.put("message", "Erreurs de validation des entrées: " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Users user = usersRepository.getUserByUsername(username);
            questionService.updateQuestion(questionId, updatedto, user);
            response.put("message", "Mise à jour de la question réussie");
            response.put("redirectUrl", "/question/search");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.put("message", "Erreurs inattendues: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
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


    @GetMapping("/answer") // accès a la vue permettant de répondre à une question donnée
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


    @PostMapping("/answer/submit") // envoie la réponse à une question donnée
    @ResponseBody
    public ResponseEntity<?> submitAnswer(@ModelAttribute @Validated AnswerQuestionDTO answerDTO, @RequestParam("questionId") Integer questionId, BindingResult bindingResult, Model model)
    {
        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            return ResponseEntity.badRequest().body("Pas de réponse entrée: " + bindingResult.getAllErrors());


        }

        Optional<Question> optionnalQuestion = questionRepository.findById(questionId);
        if (optionnalQuestion.isPresent())
        {
            try
            {

                Question question = optionnalQuestion.get();
                String correctAnswer = question.getExpectedanswer();
                String userAnswer = answerDTO.getAnswer();
                String questionContent=question.getContent();
                String validationResponse = questionService.callOpenAIApiForValidation(correctAnswer, userAnswer, questionContent);
                validationResponse = validationResponse.toLowerCase(); // On met la réponse en minuscule pour éviter les problèmes de casse
                validationResponse = validationResponse.substring(0, Math.min(9, validationResponse.length())); // Renvoie les 9 premiers caractères de la réponse de l'API
                // .substring(0, N) renvoie une sous-chaîne qui commence à l'indice 0 et se termine à l'indice N de la chaîne originale. L'indice 0 correspond au premier caractère de la
                // chaîne. L'indice N est exclusif, ce qui signifie que le caractère à cet indice n'est pas inclus dans le résultat.
                // Math.min(a, b) renvoie le plus petit de deux nombres, a et b. Ici, il compare 9 à la longueur totale de la chaîne validationResponse.

                if (validationResponse.equals("correct")) // Si la réponse de l'API est "correct", cela signifie que la réponse de l'utilisateur est correcte
                {
                    response.put("message", "Reponse correcte");
                    response.put("status", "correct");
                    return ResponseEntity.ok().body(response);
                }
                else if (validationResponse.equals("incorrect"))
                {
                    response.put("message", "Reponse incorrecte");
                    response.put("status", "incorrect");
                    return ResponseEntity.ok().body(response);
                }

                response.put("message", "ChatGPT a généré une réponse inattendue");
                response.put("status", "unexpected");
                return ResponseEntity.ok().body(response);
            }

            catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
            // exceptions qui descendent de cette classe.
            {
                response.put("message", "Erreurs inattendues: " + e.getMessage());
                response.put("status", "error");
                return ResponseEntity.status(500).body(response);
            }

        }

        else
        {
            response.put("message", "La question n'a pas été trouvée dans la base de données");
            response.put("status", "not_found");
            return ResponseEntity.status(404).body(response);

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

    @GetMapping("/resultAnswer")
    public String resultAnswer() {
        return "resultAnswer";
    }

}



