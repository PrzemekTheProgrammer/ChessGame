package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

import java.util.List;

public interface Board {

    void setBoard();
    void addPiece(Piece piece);

    List<Piece> pieces();

    Piece getPiece(PieceCoordinate pieceCoordinate);
    void removePiece(PieceCoordinate pieceCoordinate);

}
