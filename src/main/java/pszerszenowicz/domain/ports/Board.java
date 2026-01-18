package pszerszenowicz.domain.ports;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

import java.util.List;
import java.util.Set;

public interface Board {

    PieceColor white();
    PieceColor black();

    Set<? extends Move> availableMoves(PieceColor p);

    void applyMove(Move move);
    void undoMove(Move move);
    void setBoard();
    void addPiece(Piece piece);

    List<Piece> pieces();

    Piece getPieceAtCoordinate(PieceCoordinate pieceCoordinate);

}
