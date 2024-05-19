package com.example.DevNote.UsersTest;


import com.example.DevNote.DTO.UsersLoginDTO;
import com.example.DevNote.controller.UsersController;
import com.example.DevNote.security.JwtAuthenticationResponse;
import com.example.DevNote.security.TokenProvider;
import com.example.DevNote.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private UsersController usersController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext(); // Efface tout contexte de sécurité (c'est-à-dire utilisateur authentifié) des tests précédents pour éviter les effets secondaires.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testLoginUser() {

        UsersLoginDTO loginDTO = new UsersLoginDTO();
        loginDTO.setUsername("user");
        loginDTO.setPassword("password");

        Set<ConstraintViolation<UsersLoginDTO>> violations = validator.validate(loginDTO); // validation des entrées
        if (!violations.isEmpty()) {
            fail("La validation des entrées a échoué: " + violations);
        }

        Authentication auth = mock(Authentication.class); // Crée un objet Authentication simulé pour simuler le processus d'authentification.
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( // lorsque le authenticationManager est sollicité pour authentifier un utilisateur avec le nom
                                                                                        // d'utilisateur et le mot de passe donnés, il doit retourner l'objet Authentication simulé.
                loginDTO.getUsername(), loginDTO.getPassword()))).thenReturn(auth);

        String expectedToken = "token";
        when(tokenProvider.generateToken(auth)).thenReturn(expectedToken);
        // Simulation de la génération de token Spécifie que lorsque le tokenProvider est sollicité pour générer un token pour l'utilisateur authentifié, il doit retourner "token".
        ResponseEntity<?> response = usersController.loginUser(loginDTO); // Appelle la méthode loginUser de votre contrôleur avec le DTO de connexion (loginDTO) qui traite les données
        // de connexion, authentifie l'utilisateur et retourne une réponse contenant le JWT si l'authentification est réussie.

        assertEquals(200, response.getStatusCodeValue()); // Vérifie que le code de statut HTTP de la réponse est 200, ce qui indique une réponse réussie.

        JwtAuthenticationResponse jwtResponse = (JwtAuthenticationResponse) response.getBody(); // Cette ligne extrait le corps de la réponse HTTP, qui est une instance de
        // JwtAuthenticationResponse, contenant le JWT généré.

        assertNotNull(jwtResponse); // Vérifie que la réponse contient bien le token
        assertEquals(expectedToken, jwtResponse.getAccessToken()); // Vérifie que le token est bien celui attendu
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        // Utilise Mockito pour vérifier que la méthode authenticate de l'AuthenticationManager a été appelée exactement une fois.
        verify(tokenProvider, times(1)).generateToken(any(Authentication.class));
        //  Vérifie que la méthode generateToken du TokenProvider a été appelée une fois avec n'importe quel objet de type Authentication
    }
}
