package com.example.DevNote.service;
import com.example.DevNote.DTO.CreateQuestionDTO;

import com.example.DevNote.model.Access;
import com.example.DevNote.model.Question;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.QuestionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;  // permet d'accéder aux variables d'environnement définies dans application.properties

    // Méthode pour créer une question avec le minimum de champs obligatoires possibles
    public Question createQuestionMinimum(String content, String expectedanswer, Access access, Users user) {
        Question question = new Question(content, expectedanswer, access, user);
        questionRepository.save(question);
        return question;
    }


    @Transactional
    public Question createQuestion(CreateQuestionDTO dto, Users user) throws Exception  // NB: très important : Quand tu définis une exception dans une méthode, quand tu appelles cette méthode,
    // il faut utiliser un try catch ou un autre moyen de gérer les exceptions
    {
        if (questionRepository.existsByContentAndUser(dto.getContent(), user))
        {
            throw new Exception("Cette question existe déjà pour cet utilisateur.");
        }

        Access access = dto.getAccess(); // Détermine si la question est publique ou privée
        Question question = new Question(dto.getContent(), dto.getExpectedanswer(), access, user);
        questionRepository.save(question);
        return question;

    }

    public List<Question> getQuestionsForUserAndPublic(Users user) {
        return questionRepository.findByUserOrAccess(user, Access.publique);
    }


    public String callOpenAIApiForValidation(String correctAnswer, String userAnswer, String questionContent) {
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

}