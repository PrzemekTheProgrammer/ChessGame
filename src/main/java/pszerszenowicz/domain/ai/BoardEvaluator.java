package pszerszenowicz.domain.ai;

import pszerszenowicz.domain.ai.transposition.EvaluatorId;
import pszerszenowicz.domain.ports.game.Position;

public interface BoardEvaluator {
    int evaluate(Position position);
    EvaluatorId getId();
}
