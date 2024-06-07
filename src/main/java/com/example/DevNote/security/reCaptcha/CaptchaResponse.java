package com.example.DevNote.security.reCaptcha;

public class CaptchaResponse {  // Cette classe est un POJO qui contient la réponse de l'API de vérification de Google reCAPTCHA :

    // les attributs success, challenge_ts et hostname correspondent aux champs de la réponse JSON renvoyée par l'API de vérification de reCAPTCHA de Google.
    // Cette classe sert à désérialiser (convertir) la réponse JSON en un objet Java. Avoir un objet Java spécifique rend le code plus facile à manipuler. On peut accéder aux
    // données via des méthodes getter plutôt que de manipuler directement des chaînes ou des structures de données complexes comme des Map. Cela rend le code plus lisible
    // et plus facile à maintenir.

    private boolean success;   // indique si le test captcha a été passé avec succès
    private String challenge_ts; // timestamp de la réponse captcha
    private String hostname; // Le nom de domaine où le captcha a été résolu

    // Getters et Setters:

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallenge_ts() {
        return challenge_ts;
    }

    public void setChallenge_ts(String challenge_ts) {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


}