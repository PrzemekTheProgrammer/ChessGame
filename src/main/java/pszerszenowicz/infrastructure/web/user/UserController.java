package pszerszenowicz.infrastructure.web.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pszerszenowicz.application.dto.AuthResult;
import pszerszenowicz.application.dto.LoginCommand;
import pszerszenowicz.application.dto.RegisterCommand;
import pszerszenowicz.application.user.UserService;
import pszerszenowicz.infrastructure.web.auth.dto.AuthResponse;
import pszerszenowicz.infrastructure.web.auth.dto.LoginRequest;
import pszerszenowicz.infrastructure.web.auth.dto.RegisterRequest;

@RestController
@RequestMapping("/player")
public class UserController {

    private final UserService userService;

    public UserController(UserService playerService) {
        this.userService = playerService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {
        AuthResult result = userService.register(
                new RegisterCommand(req.username(), req.password())
        );
        return new AuthResponse(result.token());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        AuthResult result = userService.login(
                new LoginCommand(req.username(), req.password())
        );
        return new AuthResponse(result.token());
    }

}
