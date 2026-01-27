package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.ports.GameContext;
import pszerszenowicz.games.chess.move.ChessMove;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ChessContext implements GameContext {

    private final List<ChessMove> history;

    public ChessContext(List<ChessMove> history) {
        this.history = Collections.unmodifiableList(history);
    }

    @Override
    public Optional<ChessMove> lastMove() {
        return history.isEmpty() ? Optional.empty() : Optional.of(history.get(history.size()-1));
    }
}
