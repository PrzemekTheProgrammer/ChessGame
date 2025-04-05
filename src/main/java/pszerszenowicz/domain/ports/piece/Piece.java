package pszerszenowicz.domain.ports.piece;

import pszerszenowicz.adapters.Player;
import pszerszenowicz.domain.ports.board.HorizontalBoardNotation;
import pszerszenowicz.domain.ports.board.VerticalBoardNotation;

public abstract class Piece {

    private PieceCoordinate pieceCoordinate;
    private final Player player;

    public Piece(PieceCoordinate pieceCoordinate, Player player) {
        this.pieceCoordinate = pieceCoordinate;
        this.player = player;
    }

    public Piece(HorizontalBoardNotation horizontalBoardNotation,VerticalBoardNotation verticalBoardNotation, Player player) {
        this.pieceCoordinate = new PieceCoordinate(horizontalBoardNotation, verticalBoardNotation);
        this.player = player;
    }

    public PieceCoordinate getPieceCoordinate() {
        return pieceCoordinate;
    }

    public void setPieceCoordinate(PieceCoordinate pieceCoordinate) {
        this.pieceCoordinate = pieceCoordinate;
    }

    public void setPieceCoordinate(HorizontalBoardNotation horizontalBoardNotation, VerticalBoardNotation verticalBoardNotation) {
        this.pieceCoordinate.setHorizontalBoardNotation(horizontalBoardNotation);
        this.pieceCoordinate.setHorizontalBoardNotation(horizontalBoardNotation);
    }

    public Player getPlayer() {
        return player;
    }
}
