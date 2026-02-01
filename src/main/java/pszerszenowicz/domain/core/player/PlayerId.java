package pszerszenowicz.domain.core.player;

import java.util.UUID;

public record PlayerId(UUID uuid) {
    public static PlayerId random() {
        return new PlayerId(UUID.randomUUID());
    }
    public static PlayerId of(UUID uuid) {
        return new PlayerId(uuid);
    }
}
