package pszerszenowicz.domain.core.game;

import pszerszenowicz.domain.core.user.UserId;

import java.util.UUID;

public record GameId(UUID id) {
    public static GameId random() {
        return new GameId(UUID.randomUUID());
    }
    public static GameId of(UUID uuid) {
        return new GameId(uuid);
    }
}
