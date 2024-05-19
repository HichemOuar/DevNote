package com.example.DevNote.security;

public class JwtAuthenticationResponse { //  permet de structurer la réponse de ton API de manière cohérente et prévisible (cette classe indique clairement les champs à renseigner
    // dans le constructeur (pour le moment accessToken), et facilite l'évolutivité :  Plutôt que de juste renvoyer un
    // token JWT (une simple chaîne de texte), l'envelopper dans une classe comme JwtAuthenticationResponse permet de garder l'API organisée. Et si jamais on décides d'ajouter plus
    // d'informations à renvoyer après l'authentification (comme un refreshToken ou d'autres données), on peut simplement ajouter ces champs à JwtAuthenticationResponse sans changer la
    // façon dont l'API est utilisée par les autres.

    private String accessToken;
    private String tokenType = "Bearer";  // Le "Bearer" est un type de token d'authentification défini par le standard OAuth 2.0. Il est utilisé pour indiquer qu'un certain token permet
                                         // à son porteur (celui qui présente le token) d'accéder à la ressource protégée. On le définit ici car en incluant tokenType dans la réponse,
                                        // on indique clairement aux développeurs qui utilisent l'API comment ils doivent utiliser le token. Cela aide à prévenir les erreurs, comme
                                       // l'omission du préfixe "Bearer" qui pourrait entraîner des échecs d'authentification. On a pas besoin de le définir dans le constructeur car
                                      // La valeur ne change jamais.

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // getters et setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    // Pas de setter pour tokenType pour conserver "Bearer" comme valeur fixe
}