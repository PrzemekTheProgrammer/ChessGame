package pszerszenowicz.application.invitation.exception;

public class InvalidInvitationStateException extends RuntimeException {
    public InvalidInvitationStateException() {
        super("Invitation is not actual");
    }
}
