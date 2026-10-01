package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.games.chess.game.GameStatus;
import pszerszenowicz.games.chess.move.ChessMove;

import java.util.Set;

public interface Position {

    Set<ChessMove> availableMoves();
    Set<ChessMove> legalMoves();
    GameStatus evaluateGameState ();
    PieceColor getSideToMove();
    Board getBoard();
    Long zobristHash();

}
