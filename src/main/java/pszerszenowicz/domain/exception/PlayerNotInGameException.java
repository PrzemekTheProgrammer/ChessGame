package pszerszenowicz.domain.exception;

public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException() {
        super("You are not part of this game.");
    }
}
