package pszerszenowicz.application.invitation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pszerszenowicz.application.game.GameService;
import pszerszenowicz.application.invitation.exception.InvitationNotFoundException;
import pszerszenowicz.application.ports.PlayerFactory;
import pszerszenowicz.application.ports.invitation.InvitationRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private InvitationRepository repo;

    @Mock
    private GameService gameService;

    @Mock
    private PlayerFactory playerFactory;

    @InjectMocks
    private InvitationService service;

    private final UserId from = UserId.random();
    private final UserId to = UserId.random();

    @Test
    void should_create_invitation() {
        InvitationId id = service.invite(
                from,
                to,
                ColorChoice.RANDOM
        );

        assertNotNull(id);
        verify(repo).save(any(GameInvitation.class));
    }

    @Test
    void should_accept_invitation_and_create_game() {
        InvitationId id = InvitationId.random();
        GameInvitation inv = GameInvitation.restore(
                id,
                from,
                to,
                InvitationStatus.PENDING,
                ColorChoice.INVITER_WHITE
        );

        when(repo.findById(id)).thenReturn(Optional.of(inv));
        when(playerFactory.createHuman(any()))
                .thenReturn(mock(Player.class));

        GameId gameId = GameId.random();
        when(gameService.createGame(any(), any()))
                .thenReturn(gameId);

        GameId result = service.accept(id, to);

        assertEquals(gameId, result);

        verify(repo).delete(id);
        verify(gameService).createGame(any(), any());
    }

    @Test
    void should_reject_invitation() {
        InvitationId id = InvitationId.random();
        GameInvitation inv = GameInvitation.restore(
                id,
                from,
                to,
                InvitationStatus.PENDING,
                ColorChoice.RANDOM
        );

        when(repo.findById(id)).thenReturn(Optional.of(inv));

        service.reject(id, to);

        verify(repo).delete(id);
    }

    @Test
    void should_throw_if_invitation_not_found() {
        InvitationId id = InvitationId.random();

        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                InvitationNotFoundException.class,
                () -> service.accept(id, to)
        );
    }
}