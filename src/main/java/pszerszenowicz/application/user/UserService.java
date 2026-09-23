package pszerszenowicz.application.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pszerszenowicz.application.dto.AuthResult;
import pszerszenowicz.application.dto.LoginCommand;
import pszerszenowicz.application.dto.RegisterCommand;
import pszerszenowicz.domain.core.user.User;
import pszerszenowicz.application.exception.InvalidCredentialsException;
import pszerszenowicz.domain.exception.UsernameAlreadyExistsException;
import pszerszenowicz.application.ports.user.UserRepository;
import pszerszenowicz.infrastructure.security.jwt.JwtService;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository playerRepo,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepo = playerRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResult register(RegisterCommand req) {
        userRepo.findByUsername(req.username())
                .ifPresent(p -> { throw new UsernameAlreadyExistsException(req.username()); });

        User user = User.register(
                req.username(),
                req.password(),
                passwordEncoder);

        userRepo.save(user);
        String token = jwtService.generate(user.getId().uuid());
        return new AuthResult(token);
    }

    public AuthResult login(LoginCommand req) {
        User player = userRepo.findByUsername(req.username())
                .orElseThrow(InvalidCredentialsException::new);

        if(!player.passwordMatches(req.password(),passwordEncoder)) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generate(player.getId().uuid());
        return new AuthResult(token);
    }
}
