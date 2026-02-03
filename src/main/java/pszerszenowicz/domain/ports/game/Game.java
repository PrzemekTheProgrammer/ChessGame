package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.game.GameId;

public interface Game {

    void makeMove(Move move, Player player);
    void initGame();
    GameId getGameId();
    boolean hasPlayer(Player p);
}
