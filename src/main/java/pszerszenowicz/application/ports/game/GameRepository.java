package pszerszenowicz.application.ports.game;

import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.games.chess.game.ChessGame;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    void save(ChessGame game);
    Optional<ChessGame> find(GameId id);
    List<ChessGame> findByUserId(UserId userId);
}
