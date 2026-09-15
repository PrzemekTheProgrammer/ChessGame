package pszerszenowicz.application.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pszerszenowicz.application.ports.PlayerFactory;
import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.infrastructure.ai.AiEngineFactory;

@Component
public class DefaultPlayerFactory implements PlayerFactory {

    @Autowired
    private AiEngineFactory aiEngineFactory;

    @Override
    public Player createHuman(UserId userId) {
        return new HumanPlayer(userId);
    }

    @Override
    public Player createBot(AiType aiType) {
        return new BotPlayer(aiEngineFactory.getEngine(aiType));
    }
}
