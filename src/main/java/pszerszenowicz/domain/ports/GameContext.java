package pszerszenowicz.domain.ports;

import java.util.Optional;

public interface GameContext {

    public Optional<? extends Move> lastMove();

}
