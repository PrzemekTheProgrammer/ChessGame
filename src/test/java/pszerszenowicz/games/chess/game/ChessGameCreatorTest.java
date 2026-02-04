package pszerszenowicz.games.chess.game;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ChessGameCreatorTest {

    private ChessGameCreator creator = new ChessGameCreator();

    @Test
    void shouldCreateChessGame() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        Game game = creator.create(p1,p2);

        assertNotNull(game);
        assertTrue(game instanceof ChessGame);
    }
}