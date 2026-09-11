package pszerszenowicz.application.invitation;

import pszerszenowicz.application.invitation.exception.CannotInviteYourselfException;
import pszerszenowicz.application.invitation.exception.InvalidInvitationStateException;
import pszerszenowicz.application.invitation.exception.InvitationNotForThisUserException;
import pszerszenowicz.domain.core.user.UserId;

import java.util.Random;

public class GameInvitation {
    private final InvitationId id;
    private final UserId from;
    private final UserId to;
    private InvitationStatus status;
    private final ColorChoice colorChoice;

    GameInvitation(UserId from, UserId to, ColorChoice colorChoice) {
        if (from.equals(to)) {
            throw new CannotInviteYourselfException();
        }
        this.from = from;
        this.to = to;
        this.colorChoice = colorChoice;
        id = InvitationId.random();
        status = InvitationStatus.PENDING;
    }

    protected GameInvitation(InvitationId id,
                             UserId from,
                             UserId to,
                             InvitationStatus status,
                             ColorChoice colorChoice) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.status = status;
        this.colorChoice = colorChoice;
    }

    public static GameInvitation restore(InvitationId id,
                                         UserId from,
                                         UserId to,
                                         InvitationStatus status,
                                         ColorChoice colorChoice) {
        return new GameInvitation(id, from, to, status, colorChoice);
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

    public InvitationId getId() {
        return id;
    }

    public UserId getFrom() {
        return from;
    }

    public UserId getTo() {
        return to;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public ColorChoice getColorChoice() {
        return colorChoice;
    }

    public UserId resolveWhiteUser() {
        return switch (colorChoice) {
            case INVITER_WHITE -> from;
            case INVITER_BLACK -> to;
            case RANDOM -> RandomColorResolver.pickWhite(from, to);
        };
    }

    public UserId resolveBlackUser() {
        return resolveWhiteUser().equals(from) ? to : from;
    }

    private static class RandomColorResolver {
        private static final Random random = new Random();

        static UserId pickWhite(UserId from, UserId to) {
            return random.nextBoolean() ? from : to;
        }
    }

}
