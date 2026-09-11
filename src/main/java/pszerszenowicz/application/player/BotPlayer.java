package pszerszenowicz.application.player;

import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.ports.game.Player;

public class BotPlayer implements Player {

    private final AiType aiType;

    public BotPlayer(AiType aiType) {
        this.aiType = aiType;
    }
}
