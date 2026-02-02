package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

import java.util.List;
import java.util.Set;

public interface Board {

    Set<? extends Move> availableMoves(PieceColor p);

    void setBoard();
    void addPiece(Piece piece);

    List<Piece> pieces();

    Piece getPiece(PieceCoordinate pieceCoordinate);
    void removePiece(PieceCoordinate pieceCoordinate);

}
