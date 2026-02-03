package pszerszenowicz.application.ports.game;

import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    void save(Game game);
    Optional<Game> find(GameId id);
    List<Game> findByPlayer(Player player);
}
