package pszerszenowicz.application.ai;

import org.springframework.stereotype.Service;
import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.infrastructure.ai.AiEngineFactory;

@Service
public class AiApplicationService {

    private final AiEngineFactory aiEngineFactory;

    public AiApplicationService(AiEngineFactory aiEngineFactory) {
        this.aiEngineFactory = aiEngineFactory;
    }

    public Move findBestMove(ChessGame chessGame, AiType aiType, long timeLimit) {
        AiEngine engine = aiEngineFactory.getEngine(aiType);
        return engine.findBestMoveWithTimeLimit(chessGame, timeLimit);
    }
}
