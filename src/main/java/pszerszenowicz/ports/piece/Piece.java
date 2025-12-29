package pszerszenowicz.ports.piece;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;

import java.util.Set;

public abstract class Piece {

    private PieceCoordinate pieceCoordinate;
    private final Player player;

    public Piece(PieceCoordinate pieceCoordinate, Player player) {
        this.pieceCoordinate = pieceCoordinate;
        this.player = player;
    }

    public PieceCoordinate getPieceCoordinate() {
        return pieceCoordinate;
    }

    public void setPieceCoordinate(PieceCoordinate pieceCoordinate) {
        this.pieceCoordinate = pieceCoordinate;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract Set<Move> getMoves(Board board);

}
