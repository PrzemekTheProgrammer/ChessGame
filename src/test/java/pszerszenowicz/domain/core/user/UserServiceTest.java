package pszerszenowicz.domain.core.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pszerszenowicz.application.service.UserService;
import pszerszenowicz.application.dto.AuthResult;
import pszerszenowicz.application.dto.LoginCommand;
import pszerszenowicz.application.dto.RegisterCommand;
import pszerszenowicz.application.exception.InvalidCredentialsException;
import pszerszenowicz.domain.exception.UsernameAlreadyExistsException;
import pszerszenowicz.domain.ports.user.UserRepository;
import pszerszenowicz.infrastructure.security.jwt.JwtService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository playerRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_newUser_shouldSaveAndReturnToken() {
        //given
        RegisterCommand req = new RegisterCommand("user1", "pass123");
        //when
        when(playerRepo.findByUsername("user1")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("hashedPass");
        when(jwtService.generate(any(UUID.class))).thenReturn("jwt-token");
        //then
        AuthResult response = playerService.register(req);

        assertEquals("jwt-token", response.token());
        verify(playerRepo).save(any(User.class));
    }

    @Test
    void register_existingUsername_shouldThrowException() {
        //given
        RegisterCommand req = new RegisterCommand("user1", "pass123");
        //when
        when(playerRepo.findByUsername("user1"))
                .thenReturn(Optional.of(new User(UserId.random(),"user1", "hashedPass")));
        //then
        assertThrows(UsernameAlreadyExistsException.class, () -> playerService.register(req));
    }

    @Test
    void login_correctCredentials_shouldReturnToken() {
        //given
        LoginCommand req = new LoginCommand("user1", "pass123");
        User player = new User("user1", "hashedPass", passwordEncoder);
        //when
        when(playerRepo.findByUsername("user1")).thenReturn(Optional.of(player));
        when(player.passwordMatches("pass123", passwordEncoder)).thenReturn(true);
        when(jwtService.generate(player.getId().uuid())).thenReturn("jwt-token");
        //then
        AuthResult response = playerService.login(req);

        assertEquals("jwt-token", response.token());
    }

    @Test
    void login_wrongPassword_shouldThrowException() {
        //given
        LoginCommand req = new LoginCommand("user1", "wrongpass");
        User player = new User("user1", "hashedPass", passwordEncoder);
        //when
        when(playerRepo.findByUsername("user1")).thenReturn(Optional.of(player));
        when(player.passwordMatches("wrongpass", passwordEncoder)).thenReturn(false);
        //then
        assertThrows(InvalidCredentialsException.class, () -> playerService.login(req));
    }
}