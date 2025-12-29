package pszerszenowicz.chess.domain;

import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.ports.game.Game;

public class ChessGame extends Game{
    public ChessGame() {
        super(new ChessBoard());
    }
}
