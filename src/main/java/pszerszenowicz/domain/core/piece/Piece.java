package pszerszenowicz.domain.core.piece;

import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Position;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.Set;

public abstract class Piece {

    private PieceCoordinate pieceCoordinate;
    private final PieceColor color;

    public Piece(PieceCoordinate pieceCoordinate, PieceColor color) {
        this.pieceCoordinate = pieceCoordinate;
        this.color = color;
    }

    public PieceCoordinate getPieceCoordinate() {
        return pieceCoordinate;
    }

    public void setPieceCoordinate(PieceCoordinate pieceCoordinate) {
        this.pieceCoordinate = pieceCoordinate;
    }

    public PieceColor getColor() {
        return color;
    }

    public abstract Set<? extends Move> getMoves(ChessPosition position);

}
