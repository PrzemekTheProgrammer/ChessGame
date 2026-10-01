package pszerszenowicz.domain.exception;

import pszerszenowicz.games.chess.move.ChessMove;

public class MoveNotAvailableException extends RuntimeException {
    public MoveNotAvailableException(ChessMove move) {
        super("Illegal move: " + move);
    }
}
