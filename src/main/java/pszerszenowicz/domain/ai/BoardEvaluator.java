package pszerszenowicz.domain.ai;

import pszerszenowicz.domain.ports.game.Position;
import pszerszenowicz.games.chess.position.ChessPosition;

public interface BoardEvaluator {
    int evaluate(Position position);
}
