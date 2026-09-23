package pszerszenowicz.infrastructure.web.game;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pszerszenowicz.application.game.GameService;
import pszerszenowicz.application.game.PlayerColorChoice;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.infrastructure.security.CurrentUserProvider;

import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final CurrentUserProvider currentUser;
    private final GameStateMapper gameStateMapper;

    public GameController(
            GameService gameService,
            CurrentUserProvider currentUser,
            GameStateMapper gameStateMapper
    ) {
        this.gameService = gameService;
        this.currentUser = currentUser;
        this.gameStateMapper = gameStateMapper;
    }

    @PostMapping("/bot")
    public ResponseEntity<CreateGameResponse> createBotGame(
            @RequestBody CreateBotGameRequest request
    ) {
        PlayerColorChoice colorChoice = switch (request.color()) {
            case WHITE -> PlayerColorChoice.WHITE;
            case BLACK -> PlayerColorChoice.BLACK;
            case RANDOM -> PlayerColorChoice.RANDOM;
        };

        GameId gameId = gameService.createBotGame(
                currentUser.get(),
                request.aiType(),
                colorChoice
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new CreateGameResponse(
                                gameId.id()
                        )
                );
    }

    @GetMapping("/{id}")
    public GameStateResponse getGame(
            @PathVariable UUID id
    ) {
        UserId userId = currentUser.get();
        GameId gameId = GameId.of(id);
        return gameService.withGame(
                gameId,
                game -> gameStateMapper.toResponse(
                        game,
                        userId
                )
        );
    }

}