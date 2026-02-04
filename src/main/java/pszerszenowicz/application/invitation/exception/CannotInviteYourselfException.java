package pszerszenowicz.application.invitation.exception;

public class CannotInviteYourselfException extends RuntimeException {
    public CannotInviteYourselfException() {
        super("Cannot invite yourself");
    }
}
