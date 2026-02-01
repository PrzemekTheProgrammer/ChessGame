package pszerszenowicz.domain.ports.game;

import java.util.Optional;

public interface GameContext {

    Optional<? extends Move> lastMove();

}
