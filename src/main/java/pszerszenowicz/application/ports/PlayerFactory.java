package pszerszenowicz.application.ports;

import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;

public interface PlayerFactory {
    Player createHuman(UserId userId);
}
