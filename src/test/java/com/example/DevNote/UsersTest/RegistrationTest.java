package com.example.DevNote.UsersTest;

import com.example.DevNote.DTO.UsersRegistrationDTO;
import com.example.DevNote.model.Users;
import com.example.DevNote.repository.UsersRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Cette annotation prépare l'environnement de test pour utiliser Mockito.Cela permet d'utiliser des annotations comme @Mock et @InjectMocks pour
                                   // simuler les dépendances.

public class RegistrationTest {

    @Mock // Crée une fausse (mock) version de UsersRepository. Cela permet de contrôler les réponses des appels de méthode effectués sur ce faux repository pendant le test.
    private UsersRepository usersRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    //UsersRepository et BCryptPasswordEncoder sont marqués avec @Mock parce qu'on veut contrôler leur comportement dans les tests de UserService. On ne veut pas que les appels à la
    // base de données soient réellement exécutés ( UsersRepository) ou que les mots de passe soient réellement encodés durant les tests (passwordEncoder). Au lieu de ça,
    // on simule ces comportements pour s'assurer que UserService réagit correctement aux valeurs retournées ou aux exceptions levées par ces dépendances

    @InjectMocks // @Mock est utilisée pour créer un faux objet d'une dépendance que la classe sous test (ici UserService) utilise. @InjectMocks est utilisée pour créer une instance de
                // la classe qu'on teste, en injectant automatiquement les mocks créés avec @Mock

    private UserService userService;

    private Validator validator;  // Jakarta Bean Validation permet de s'assurer que les attributs des objets Java respectent certaines contraintes avant que ces objets soient
                                 // utilisés dans l'application.

    @BeforeEach  // Cette annotation indique que la méthode setUp() doit être exécutée avant chaque méthode de test dans la classe
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateUserOK() {
        UsersRegistrationDTO usersDTO = new UsersRegistrationDTO();
        usersDTO.setUsername("usernametest");
        usersDTO.setEmail("test@gmail.com");
        usersDTO.setCaptchaResponse("réponse ok");
        usersDTO.setPassword("motdepasseconforme2!");

        Set<ConstraintViolation<UsersRegistrationDTO>> violations = validator.validate(usersDTO); // validator.validate va en gros générer un set de violations, si violation de
                                                                                                 // contraintes il y a. Sinon, violations sera vide.
        if (!violations.isEmpty()) {
            fail("La validation des entrées a échoué: " + violations);
        }

        String encodedPassword = "encodedPassword";  // Définit une réponse simulée pour le hashage du mot de passe. C'est ce qu'on attend que le passwordEncoder retourne si il fonctionne
                                                    //  correctement

        when(passwordEncoder.encode(usersDTO.getPassword())).thenReturn(encodedPassword); // Cette ligne utilise Mockito pour configurer une simulation de la méthode encode de l'objet
        // passwordEncoder. when(...) est une fonction de Mockito utilisée pour dire "quand la méthode suivante est appelée...". Cela signifie que peu importe ce que fait réellement la
        // méthode encode, pour les besoins de ce test, elle se comportera comme si elle avait converti le mot de passe en "encodedPassword".
        // En gros, tu peux dire que dans le contexte de se texte, elle 'REDEFINIT' la méthode encode pour qu'elle se comporte différemment. MAIS ELLE N APPELLE PAS ENCORE LA METHODE
        // A CE STADE


        when(usersRepository.save(any(Users.class))).thenAnswer(i -> i.getArgument(0)); // Cette ligne, de la même façon, CONFIGURE la simulation la persistance du user en base de données:
        // la méthode save enregistre un objet dans la base de données et retourne l'objet enregistré (potentiellement modifié ou enrichi par le processus de sauvegarde, par exemple
        // avec un ID généré). L'utilisation de thenAnswer(i -> i.getArgument(0)) permet de simuler le comportement d'une méthode save qui retournerait normalement l'objet qu'elle vient
        // de sauvegarder. En test unitaire, cela signifie que l'objet Users que nous passons à la méthode save est le même objet que nous récupérerons (getArgument(0) permet de
        // récupérer le premier argument passé à la méthode interceptée, c’est-à-dire l'objet Users que nous avons tenté de sauvegarder.) Cela nous permet de vérifier que l'objet n'a pas
        // été altéré de manière inattendue durant son "passage" dans la méthode save du repository.
        //==> ON REDEFINIT EN QUELQUES SORTES ici la methode save de usersRepository, mais on ne l'appelle pas !!

        try {
            Users user = userService.createUser(usersDTO);
            assertThat(user.getUsername()).isEqualTo(usersDTO.getUsername());
            assertThat(user.getEmail()).isEqualTo(usersDTO.getEmail());
            assertThat(user.getPassword()).isEqualTo(encodedPassword);
            verify(passwordEncoder).encode(usersDTO.getPassword()); //Cette ligne s'assure que la méthode encode de l'objet passwordEncoder a été appelée avec le mot de passe fourni par usersDTO.
            verify(usersRepository).save(any(Users.class)); // Cette ligne s'assure que la méthode save du usersRepository a été appelée avec un objet de type Users.
        } catch (Exception e) {
            fail("Le test a échoué en raison d'une exception innatendue: " + e.getMessage());
        }
    }
}
