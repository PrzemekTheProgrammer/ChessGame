package pszerszenowicz.ports.exception;

import pszerszenowicz.ports.move.Move;

public class MoveNotAvailableException extends RuntimeException {
    public MoveNotAvailableException(Move move) {
        super("Illegal move: " + move);
    }
}
