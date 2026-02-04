package pszerszenowicz.application.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pszerszenowicz.application.exception.GameNotFoundException;
import pszerszenowicz.application.ports.game.GameFactory;
import pszerszenowicz.application.ports.game.GameRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameFactory factory;

    @Mock
    private GameRepository repo;

    @InjectMocks
    private GameService gameService;

    @Test
    void createGameShouldReturnGameId() {
        //given
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Game game = mock(Game.class);
        GameId id = GameId.random();
        //when
        when(factory.create(GameType.CHESS,p1,p2)).thenReturn(game);
        when(game.getGameId()).thenReturn(id);
        GameId retId = gameService.createGame(GameType.CHESS,p1,p2);
        //then
        assertEquals(id,retId);
        verify(factory).create(GameType.CHESS,p1,p2);
        verify(repo).save(game);
        verifyNoMoreInteractions(factory,repo);
    }

    @Test
    void makeMove_shouldLoadGame_executeMove_andSave() {
        // given
        GameId id = GameId.random();
        Move move = mock(Move.class);
        Player player = mock(Player.class);
        Game game = mock(Game.class);

        when(repo.find(id)).thenReturn(Optional.of(game));

        // when
        gameService.makeMove(id, move, player);

        // then
        verify(repo).find(id);
        verify(game).makeMove(move, player);
        verify(repo).save(game);
        verifyNoMoreInteractions(repo, game);
    }

    @Test
    void makeMove_shouldThrowIfGameNotFound() {
        // given
        GameId id = GameId.random();
        Move move = mock(Move.class);
        Player player = mock(Player.class);

        when(repo.find(id)).thenReturn(Optional.empty());

        // when + then
        assertThrows(GameNotFoundException.class,
                () -> gameService.makeMove(id, move, player));

        verify(repo).find(id);
        verifyNoMoreInteractions(repo);
    }

    // ---------- myGames ----------

    @Test
    void myGames_shouldReturnGamesFromRepository() {
        // given
        Player player = mock(Player.class);
        List<Game> games = List.of(mock(Game.class), mock(Game.class));

        when(repo.findByPlayer(player)).thenReturn(games);

        // when
        List<Game> result = gameService.myGames(player);

        // then
        assertEquals(games, result);

        verify(repo).findByPlayer(player);
        verifyNoMoreInteractions(repo);
    }

}
