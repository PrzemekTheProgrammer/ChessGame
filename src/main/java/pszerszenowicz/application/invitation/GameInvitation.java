package pszerszenowicz.application.invitation;

import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.application.invitation.exception.CannotInviteYourselfException;
import pszerszenowicz.application.invitation.exception.InvalidInvitationStateException;
import pszerszenowicz.application.invitation.exception.InvitationNotForThisUserException;
import pszerszenowicz.domain.core.user.UserId;

public class GameInvitation {
    private final InvitationId id;
    private final UserId from;
    private final UserId to;
    private final GameType gameType;
    private InvitationStatus status;
    private final ColorChoice colorChoice;

    GameInvitation(UserId from, UserId to, GameType gameType, ColorChoice colorChoice) {
        if(from.equals(to)) {
            throw new CannotInviteYourselfException();
        }
        this.from = from;
        this.to = to;
        this.gameType = gameType;
        this.colorChoice = colorChoice;
        id = InvitationId.random();
        status = InvitationStatus.PENDING;
    }

    public void accept(UserId user) {
        validateRecipient(user);
        status = InvitationStatus.ACCEPTED;
    }

    public void decline(UserId user) {
        validateRecipient(user);
        status = InvitationStatus.REJECTED;
    }

    private void validateRecipient(UserId user) {
        if (!to.equals(user)) {
            throw new InvitationNotForThisUserException();
        }
        if (status != InvitationStatus.PENDING) {
            throw new InvalidInvitationStateException();
        }
    }

}
