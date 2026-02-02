package pszerszenowicz.domain.exception;

public class MoveNotAvailableForPlayerException extends RuntimeException {
    public MoveNotAvailableForPlayerException() {
        super("You are not actual Player to take a move");
    }
}
