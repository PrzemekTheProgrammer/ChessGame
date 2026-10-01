package pszerszenowicz.domain.ports.game;

import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.games.chess.game.GameStatus;

public interface Game {

    void makeMove(Move move, Player player);
    void initGame();
    GameStatus getStatus();
    Position getPosition();
    GameId getGameId();
    boolean hasPlayer(Player p);
}
