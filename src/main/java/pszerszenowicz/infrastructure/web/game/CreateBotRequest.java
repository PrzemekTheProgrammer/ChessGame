package pszerszenowicz.infrastructure.web.game;

import pszerszenowicz.application.game.PlayerColorChoice;
import pszerszenowicz.domain.ai.AiType;

public record CreateBotRequest(
        AiType aiType,
        PlayerColorChoice color
) {
}
