package pszerszenowicz.domain.ports.game;

import java.util.Optional;

public interface GameContext {

    public Optional<? extends Move> lastMove();

}
