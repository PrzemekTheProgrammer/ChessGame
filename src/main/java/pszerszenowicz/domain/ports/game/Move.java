package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.position.ChessPosition;

public interface Move {
    void addTag(ChessMoveTags tag);
    boolean hasTag(ChessMoveTags tag);
    PieceCoordinate to();
    PieceCoordinate from();
    Piece piece();
    void apply(ChessPosition position);
    void undo(ChessPosition position);


}
