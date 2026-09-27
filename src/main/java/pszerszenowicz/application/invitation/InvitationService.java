package pszerszenowicz.application.invitation;

import org.springframework.stereotype.Service;
import pszerszenowicz.application.game.GameService;
import pszerszenowicz.application.invitation.exception.InvitationNotFoundException;
import pszerszenowicz.application.ports.PlayerFactory;
import pszerszenowicz.application.ports.invitation.InvitationRepository;
import pszerszenowicz.application.ports.user.UserRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.User;
import pszerszenowicz.domain.core.user.UserId;

import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository repo;
    private final GameService gameService;
    private final PlayerFactory playerFactory;
    private final UserRepository userRepository;

    public InvitationService(InvitationRepository repo,
                             GameService gameService,
                             PlayerFactory playerFactory,
                             UserRepository userRepository)
    {
        this.repo = repo;
        this.gameService = gameService;
        this.playerFactory = playerFactory;
        this.userRepository = userRepository;
    }

    public InvitationId invite(UserId from, UserId to, ColorChoice colorChoice) {
        GameInvitation inv = new  GameInvitation(from, to, colorChoice);
        repo.save(inv);
        return inv.getId();
    }

    public GameId accept(InvitationId id, UserId user) {
        GameInvitation inv = repo.findById(id)
                .orElseThrow(InvitationNotFoundException::new);

        inv.accept(user);

        GameInvitation.ResolvedColors colors = inv.resolveColors();

        GameId gameId = gameService.createGame(
                playerFactory.createHuman(colors.white()),
                playerFactory.createHuman(colors.black())
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

    public List<InvitationResult> receivedInvitations(UserId userId) {
        return repo.findReceivedByUser(userId)
                .stream()
                .map(invitation -> {
                    User fromUser = userRepository
                            .findById(invitation.getFrom().uuid())
                            .orElseThrow();

                    return new InvitationResult(
                            invitation.getId().id(),
                            invitation.getFrom().uuid(),
                            fromUser.getUserName(),
                            invitation.getColorChoice()
                    );
                })
                .toList();
    }
}
