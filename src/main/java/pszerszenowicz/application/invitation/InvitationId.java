package pszerszenowicz.application.invitation;

import pszerszenowicz.domain.core.user.UserId;

import java.util.UUID;

public record InvitationId(UUID id) {
    public static InvitationId random() {
        return new InvitationId(UUID.randomUUID());
    }
    public static InvitationId of(UUID uuid) {
        return new InvitationId(uuid);
    }
}
