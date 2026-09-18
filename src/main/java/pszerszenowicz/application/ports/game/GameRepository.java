package pszerszenowicz.application.ports.game;

import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    void save(ChessGame game);
    Optional<ChessGame> find(GameId id);
    List<ChessGame> findByPlayer(Player player);
}
