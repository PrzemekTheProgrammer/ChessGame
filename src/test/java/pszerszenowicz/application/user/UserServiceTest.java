package pszerszenowicz.application.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pszerszenowicz.application.dto.AuthResult;
import pszerszenowicz.application.dto.LoginCommand;
import pszerszenowicz.application.dto.RegisterCommand;
import pszerszenowicz.application.exception.InvalidCredentialsException;
import pszerszenowicz.application.ports.user.UserRepository;
import pszerszenowicz.domain.core.user.User;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.exception.UsernameAlreadyExistsException;
import pszerszenowicz.infrastructure.security.jwt.JwtService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_existingUsername_shouldThrowException() {
        //given
        RegisterCommand req = new RegisterCommand("user1", "pass123");

        when(userRepo.findByUsername("user1"))
                .thenReturn(Optional.of(User.restore(UserId.random(), "user1", "hashedPass")));

        //then
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.register(req));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void login_validCredentials_shouldReturnToken() {
        //given
        LoginCommand req = new LoginCommand("user1", "pass123");
        User user = User.restore(UserId.random(), "user1", "hashedPass");

        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "hashedPass")).thenReturn(true);
        when(jwtService.generate(user.getId().uuid())).thenReturn("jwt-token");

        //when
        AuthResult result = userService.login(req);

        //then
        assertEquals("jwt-token", result.token());
    }

    @Test
    void login_invalidPassword_shouldThrowException() {
        //given
        LoginCommand req = new LoginCommand("user1", "wrongpass");
        User user = User.restore(UserId.random(), "user1", "hashedPass");

        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashedPass")).thenReturn(false);

        //then
        assertThrows(InvalidCredentialsException.class, () -> userService.login(req));
    }
}