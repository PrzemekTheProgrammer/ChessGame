package pszerszenowicz.infrastructure.web.game;

import pszerszenowicz.domain.ai.AiType;

public record CreateBotGameRequest(
        AiType aiType,
        BotColor color
) {
}
