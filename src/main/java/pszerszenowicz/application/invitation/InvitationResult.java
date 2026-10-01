package pszerszenowicz.application.invitation;

import java.util.UUID;

public record InvitationResult(
        UUID invitationId,
        UUID fromUserId,
        String fromUsername,
        ColorChoice colorChoice
) {
}