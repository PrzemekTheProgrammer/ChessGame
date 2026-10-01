package pszerszenowicz.application.ports;

import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;

public interface PlayerFactory {
    Player createHuman(UserId userId);
    Player createBot(AiType aiType);
}
