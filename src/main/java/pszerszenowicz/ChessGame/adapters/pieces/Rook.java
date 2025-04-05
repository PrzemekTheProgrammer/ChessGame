package pszerszenowicz.ChessGame.adapters.pieces;

import pszerszenowicz.adapters.Player;
import pszerszenowicz.domain.ports.board.HorizontalBoardNotation;
import pszerszenowicz.domain.ports.board.VerticalBoardNotation;
import pszerszenowicz.domain.ports.piece.Piece;
import pszerszenowicz.domain.ports.piece.PieceCoordinate;

public class Rook extends Piece {
    public Rook(PieceCoordinate pieceCoordinate, Player player) {
        super(pieceCoordinate, player);
    }

    public Rook(HorizontalBoardNotation horizontalBoardNotation, VerticalBoardNotation verticalBoardNotation, Player player) {
        super(horizontalBoardNotation, verticalBoardNotation, player);
    }
}
