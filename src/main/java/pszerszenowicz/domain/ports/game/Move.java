package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.core.piece.Piece;

public interface Move {
    void addTag(MoveTag tag);
    boolean hasTag(MoveTag tag);
    PieceCoordinate to();
    PieceCoordinate from();
    Piece piece();
    void apply(Board board);
    void undo(Board board);


}
