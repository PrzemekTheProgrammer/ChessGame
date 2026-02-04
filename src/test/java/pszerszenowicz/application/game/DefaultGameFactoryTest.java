package pszerszenowicz.application.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pszerszenowicz.application.exception.UnsupportedGameTypeException;
import pszerszenowicz.application.ports.game.GameCreator;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultGameFactoryTest {

    @Mock
    private GameCreator chessCreator;

    private DefaultGameFactory factory;

    @BeforeEach
    void setUp() {
        when(chessCreator.gameType()).thenReturn(GameType.CHESS);
        factory = new DefaultGameFactory(Set.of(chessCreator));
    }

    @Test
    void shouldDelegateCreationToProperCreator() {
        //given
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Game game = mock(Game.class);

        when(chessCreator.create(p1,p2)).thenReturn(game);

        //when
        Game result = factory.create(GameType.CHESS,p1,p2);

        //then
        assertSame(game,result);
        verify(chessCreator).create(p1,p2);
        verifyNoMoreInteractions(chessCreator);
    }

    @Test
    void shouldThrowWhenGameTypeNotSupported() {
        factory = new DefaultGameFactory(Set.of()); // brak creatorów

        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        assertThrows(UnsupportedGameTypeException.class,
                () -> factory.create(GameType.CHESS,p1,p2));
    }

}
