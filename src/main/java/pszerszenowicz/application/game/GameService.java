package pszerszenowicz.application.game;

import org.springframework.stereotype.Service;
import pszerszenowicz.application.exception.GameNotFoundException;
import pszerszenowicz.application.ports.game.GameRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;

import java.util.List;

@Service
public class GameService {
    private final GameRepository repo;

    public GameService(GameRepository repo) {
        this.repo = repo;
    }

    public GameId createGame(Player p1, Player p2) {
        Game game = new ChessGame(p1,p2);
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
