package pszerszenowicz.infrastructure.persistence.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.ports.game.GameRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryGameRepository implements GameRepository {

    private final Map<GameId, Game> games = new ConcurrentHashMap<>();

    @Override
    public void save(Game game) {
        games.put(game.getGameId(), game);
    }

    @Override
    public Optional<Game> find(GameId id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public List<Game> findByPlayer(Player player) {
        return games.values().stream()
                .filter(g -> g.hasPlayer(player))
                .toList();
    }
}
