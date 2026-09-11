package pszerszenowicz.application.player;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.ports.PlayerFactory;
import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;

@Component
public class DefaultPlayerFactory implements PlayerFactory {

    @Override
    public Player createHuman(UserId userId) {
        return new HumanPlayer(userId);
    }

    @Override
    public Player createBot(AiType aiType) {
        return new BotPlayer(aiType);
    }
}
