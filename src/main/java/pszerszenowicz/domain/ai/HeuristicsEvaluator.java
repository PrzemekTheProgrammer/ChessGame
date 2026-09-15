package pszerszenowicz.domain.ai;

import pszerszenowicz.domain.ai.transposition.EvaluatorId;
import pszerszenowicz.domain.ports.game.Position;

public class HeuristicsEvaluator implements BoardEvaluator{
    @Override
    public int evaluate(Position position) {
        return position.getBoard().pieces().stream()
                .mapToInt((piece -> {
                    if(piece.getColor().equals(position.getSideToMove())) {
                        return piece.getValue();
                    } else {
                        return -piece.getValue();
                    }
                })).sum();
    }

    @Override
    public EvaluatorId getId() {
        return EvaluatorId.HEURISTIC;
    }
}
