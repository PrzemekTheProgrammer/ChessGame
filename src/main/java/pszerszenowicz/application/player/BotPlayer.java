package pszerszenowicz.application.player;

import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.position.ChessPosition;

public class BotPlayer implements Player {

    private final AiEngine aiEngine;

    public BotPlayer(AiEngine aiEngine) {
        this.aiEngine = aiEngine;
    }

    public Move findBestMove(
            ChessPosition position,
            long timeLimit
    ) {
        return aiEngine.findBestMoveWithTimeLimit(
                position,
                timeLimit
        );
    }
}
