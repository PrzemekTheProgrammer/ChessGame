package pszerszenowicz.infrastructure.web.user;

import org.springframework.web.bind.annotation.*;
import pszerszenowicz.application.dto.*;
import pszerszenowicz.application.user.UserService;
import pszerszenowicz.infrastructure.web.auth.dto.AuthResponse;
import pszerszenowicz.infrastructure.web.auth.dto.LoginRequest;
import pszerszenowicz.infrastructure.web.auth.dto.RegisterRequest;

@RestController
@RequestMapping("/user")
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

    @GetMapping("/search")
    public PageResult<UserResult> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page
    ) {
        return userService.searchUsers(query, page);
    }

}
