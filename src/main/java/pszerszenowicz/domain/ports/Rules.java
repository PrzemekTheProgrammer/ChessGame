package pszerszenowicz.domain.ports;

import pszerszenowicz.domain.core.piece.PieceColor;

import java.util.Set;

public interface Rules {
    Set<? extends Move> legalMoves(Board board, PieceColor color, GameContext context);
}
