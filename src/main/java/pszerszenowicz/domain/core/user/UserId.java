package pszerszenowicz.domain.core.user;

import java.util.UUID;

public record UserId(UUID uuid) {
    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }
    public static UserId of(UUID uuid) {
        return new UserId(uuid);
    }
}
