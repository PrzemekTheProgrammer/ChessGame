package pszerszenowicz.infrastructure.persistence.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.player.HumanPlayer;
import pszerszenowicz.application.ports.game.GameRepository;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryGameRepository implements GameRepository {

    private final Map<GameId, ChessGame> games = new ConcurrentHashMap<>();

    @Override
    public void save(ChessGame game) {
        games.put(game.getGameId(), game);
    }

    @Override
    public Optional<ChessGame> find(GameId id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public List<ChessGame> findByUserId(UserId userId) {
        return games.values().stream()
                .filter(game ->
                        isUser(game.playerOf(PieceColor.WHITE), userId)
                                || isUser(game.playerOf(PieceColor.BLACK), userId)
                )
                .toList();
    }

    private boolean isUser(Player player, UserId userId) {
        return player instanceof HumanPlayer humanPlayer
                && humanPlayer.getUserId().equals(userId);
    }
}
