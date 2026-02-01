package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.piece.PieceColor;

import java.util.List;
import java.util.Set;

public interface Rules {
    Set<? extends Move> legalMoves(Board board, PieceColor color, GameContext context);
    GameStatus evaluateGameState (Set<? extends Move> legalMoves, Board board, PieceColor currentPlayer, List<? extends Move> moveHistory);
}
