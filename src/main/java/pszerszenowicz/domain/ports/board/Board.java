package pszerszenowicz.domain.ports.board;

import pszerszenowicz.adapters.Player;
import pszerszenowicz.domain.ports.piece.Piece;
import pszerszenowicz.domain.ports.piece.PieceCoordinate;

import java.util.HashMap;
import java.util.Map;

public abstract class Board {
    private Player playerOne = new Player();
    private Player playerTwo = new Player();
    protected Map<PieceCoordinate,Piece> pieces = new HashMap<>();

    public abstract void setBoard();

    public Map<PieceCoordinate, Piece> getPieces() {
        return pieces;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }
}
