package pszerszenowicz.application.invitation;

import org.springframework.stereotype.Service;
import pszerszenowicz.application.game.GameService;
import pszerszenowicz.application.invitation.exception.InvitationNotFoundException;
import pszerszenowicz.application.ports.PlayerFactory;
import pszerszenowicz.application.ports.invitation.InvitationRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.core.user.UserId;

@Service
public class InvitationService {

    private final InvitationRepository repo;
    private final GameService gameService;
    private final PlayerFactory playerFactory;

    public InvitationService(InvitationRepository repo, GameService gameService, PlayerFactory playerFactory) {
        this.repo = repo;
        this.gameService = gameService;
        this.playerFactory = playerFactory;
    }

    public InvitationId invite(UserId from, UserId to, GameType type, ColorChoice colorChoice) {
        GameInvitation inv = new  GameInvitation(from, to, type, colorChoice);
        repo.save(inv);
        return inv.getId();
    }

    public GameId accept(InvitationId id, UserId user) {
        GameInvitation inv = repo.findById(id)
                .orElseThrow(InvitationNotFoundException::new);

        inv.accept(user);

        GameId gameId = gameService.createGame(
                inv.getGameType(),
                playerFactory.createHuman(inv.resolveWhiteUser()),
                playerFactory.createHuman(inv.resolveBlackUser())
        );

        repo.delete(id);

        return gameId;
    }

    public void reject(InvitationId id, UserId user) {
        GameInvitation inv = repo.findById(id)
                .orElseThrow(InvitationNotFoundException::new);

        inv.decline(user);
        repo.delete(id);
    }
}
