package pszerszenowicz.application.ports.game;

import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

public interface GameFactory {
    Game create(GameType gameType, Player p1, Player p2);
}
