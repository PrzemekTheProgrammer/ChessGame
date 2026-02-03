package pszerszenowicz.application.service;

import org.springframework.stereotype.Service;
import pszerszenowicz.application.exception.GameNotFoundException;
import pszerszenowicz.application.ports.game.GameFactory;
import pszerszenowicz.application.ports.game.GameRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;

import java.util.List;

@Service
public class GameService {
    private final GameFactory factory;
    private final GameRepository repo;

    public GameService(GameFactory factory, GameRepository repo) {
        this.factory = factory;
        this.repo = repo;
    }

    public GameId createGame(GameType type, Player p1, Player p2) {
        Game game = factory.create(type, p1, p2);
        repo.save(game);
        return game.getGameId();
    }

    public void makeMove(GameId id, Move move, Player player) {
        Game game = repo.find(id)
                .orElseThrow(GameNotFoundException::new);
        game.makeMove(move, player);
        repo.save(game);
    }

    public List<Game> myGames(Player p) {
        return repo.findByPlayer(p);
    }

}
