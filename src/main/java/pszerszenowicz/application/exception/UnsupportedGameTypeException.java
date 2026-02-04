package pszerszenowicz.application.exception;

import pszerszenowicz.domain.core.game.GameType;

public class UnsupportedGameTypeException extends RuntimeException {
    public UnsupportedGameTypeException(GameType gameType) {
        super("Unsupported game type: " + gameType.name());
    }
}
