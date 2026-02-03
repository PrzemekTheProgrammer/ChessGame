package pszerszenowicz.games.chess.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.application.ports.game.GameCreator;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

@Component
public class ChessGameCreator implements GameCreator {
    @Override
    public GameType gameType() {
        return GameType.CHESS;
    }

    @Override
    public Game create(Player p1, Player p2) {
        return new ChessGame(p1,p2);
    }
}
