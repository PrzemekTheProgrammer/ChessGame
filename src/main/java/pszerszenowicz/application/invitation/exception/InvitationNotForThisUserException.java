package pszerszenowicz.application.invitation.exception;

public class InvitationNotForThisUserException extends RuntimeException {
    public InvitationNotForThisUserException() {
        super("This is not invitation for this user");
    }
}
