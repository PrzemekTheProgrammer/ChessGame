package pszerszenowicz.application.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pszerszenowicz.application.ports.game.GameFactory;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class GameCreationIT {

    @Autowired
    private GameFactory gameFactory;

    @Test
    void shouldCreateChessGame() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        Game game = gameFactory.create(GameType.CHESS,p1,p2);

        assertNotNull(game);
        assertInstanceOf(ChessGame.class, game);
    }

}
