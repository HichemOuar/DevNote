package com.example.DevNote.controller;


import com.example.DevNote.DTO.AnswerQuestionDTO;
import com.example.DevNote.DTO.CreateQuestionDTO;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.QuestionRepository;
import com.example.DevNote.repository.UsersRepository;
import com.example.DevNote.service.QuestionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;  // permet d'accéder aux variables d'environnement définies dans application.properties



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
    public String submitAnswer(@ModelAttribute @Validated AnswerQuestionDTO answerDTO, @RequestParam("questionId") Integer questionId, Model model, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) // On vérifie ici s'il y a des erreurs liés aux contraintes de validation du DTO
        {
            System.out.println("Pas de réponse entrée: "+(bindingResult.getAllErrors()));
            return "searchquestion";

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
                String validationResponse = callOpenAIApiForValidation(correctAnswer, userAnswer, questionContent);
                model.addAttribute("result", validationResponse);
                System.out.println("Réponse de ChatGPT pour la question: "+questionContent+ " Avec la réponse attendue: "+ correctAnswer + " et la réponse utilisateur: " + userAnswer+
                        "  :     "+validationResponse);
                return "searchquestion";
            }

            catch (Exception e) // Exception e : Exception est la classe de base pour toutes les exceptions contrôlées en Java. Capturer Exception signifie qu'on attrape toutes les
            // exceptions qui descendent de cette classe.
            {
                System.out.println("Erreurs innatendues: "+(e.getMessage())); // e.getMessage() renvoie le message d'erreur associé à l'exception capturée. Ce message est généralement fourni
                // par le constructeur de l'exception ou par une surcharge de cette exception
                return "searchquestion";
            }

        }

        else
        {
            System.out.println("La question n'a pas été trouvée dans la base de données");
            return "searchquestion";
        }


    }


    private String callOpenAIApiForValidation(String correctAnswer, String userAnswer, String questionContent) {
        String apiUrl = "https://api.openai.com/v1/chat/completions"; // URL de l'API OpenAI

        HttpHeaders headers = new HttpHeaders(); // Cette ligne crée un objet HttpHeaders pour configurer les en-têtes de la requête HTTP

        headers.setContentType(MediaType.APPLICATION_JSON); // Configure le type de contenu de la requête (JSON)

        headers.setBearerAuth(env.getProperty("openai.api.key")); // Configure le token d'autorisation en utilisant notre clé secrète



        // Construction du prompt avec un format adapté à un chat avec GPT-4. les rôles de "système" et "utilisateur" sont utilisés pour structurer le dialogue d'une manière qui aide le
        // modèle à comprendre les différentes parties de la conversation et leur contexte.

        // Le rôle "système" est utilisé pour fournir des instructions ou des informations contextuelles qui guident la conversation. Tu peux en quelques considérer que ce message a pour but
        // d'expliquer ce qui est attendu de l'API ( voici le scénario sur lequel je veux que tu te bases pour comprendre ce qui suit)

        // Le rôle "utilisateur" est utilisé pour simuler une entrée de l'utilisateur humain dans la conversation. Lorsque vous simulez une réponse de l'utilisateur, cela aide le modèle à
        // comprendre qu'il doit réagir à cette entrée comme il le ferait dans une interaction normale. Cela est particulièrement utile pour des tâches comme l'évaluation de réponses où
        // vous voulez que le modèle considère la réponse comme étant externe et non comme une continuation de ses propres "pensées".

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "Voici la question: "+questionContent+" Voici la réponse attendue: " + correctAnswer +
                                ". Et voici la réponse de l'utilisateur: " + userAnswer));
        messages.add(Map.of("role", "user", "content", "Compare la réponse attendue et la réponse fournie par l'utilisateur, en ignorant les erreurs de syntaxe, d'orthographe ou de grammaire ( Sauf si elles affectent l'execution d'un proramme ou ce genre de choses dans le cadre d'une question de programmation. Si la réponse entrée par l'utilisateur est similaire à la réponse attendue et n'omet pas d'éléments importants, réponds moi STRICTEMENT par 'Correct'. Dans le cas contraire, réponds moi STRICTEMENT par 'Incorrect'"));




        Map<String, Object> body = new HashMap<>(); // Cette ligne initialise une Map qui contiendra le corps de la requête. Une Map est une collection de paires clé-valeur.
        body.put("model", "gpt-4o"); // On veut utiliser le modèle GPT-4o

        body.put("messages", messages); // On ajoute le prompt à la Map du corps de la requête

        body.put("max_tokens", 10); // max_tokens indique le nombre maximum de tokens (mots ou blocs de texte) que l'API peut utiliser pour répondre.un token équivaut a peu
        // près a 4 caractères. Ton plan (15 dollars par mois) te donne accès a 1million de tokens ( en input et output, c’est a dire question envoyé a l’api+ pour réponse en
        // retour)

        body.put("n", 1);// Le paramètre "n" indique combien de réponses ou de complétions vous voulez que l'API génère pour le prompt donné.
        body.put("stop", null); // Le paramètre "stop" peut être utilisé pour spécifier un ou plusieurs séquences de tokens où l'API doit arrêter de générer du texte.
        // En mettant null, elle s'arrêtera donc soit après avoir généré le nombre maximum de tokens, soit quand elle atteindra une conclusion logique à sa réponse.
        body.put("temperature", 0); // . La temperature contrôle le degré de hasard dans la réponse de l'API. Une température de 0 force l'API à être moins aléatoire et plus
        // déterministe, tendant à produire les réponses les plus probables en se basant sur le prompt. Cela est utile pour des réponses où la précision est importante par
        // rapport à la créativité.

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers); // Cette ligne emballe la Map du corps et les HttpHeaders dans un objet HttpEntity, qui
        // représente la requête complète à envoyer.

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class); // Ici, la requête HTTP POST est envoyée à apiUrl avec l'objet request.
        // restTemplate est une instance de RestTemplate, utilisée pour envoyer des requêtes HTTP. Le résultat est stocké dans un objet ResponseEntity qui contiendra la
        // réponse sous forme de chaîne de caractères.

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoiceNode = choicesNode.get(0);
                JsonNode messageNode = firstChoiceNode.path("message");
                return messageNode.path("content").asText();
            } else {
                return "No valid response found";
            }
        } catch (Exception e) {
            System.out.println("Error processing JSON response: " + e.getMessage());
            return "Error processing JSON response";
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
