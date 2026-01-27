package pszerszenowicz.domain.ports;

import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.core.piece.Piece;

public interface Move {
    void addTag(Tag tag);
    boolean hasTag(Tag tag);
    PieceCoordinate to();
    PieceCoordinate from();
    Piece piece();
    void apply(Board board);
    void undo(Board board);


}
