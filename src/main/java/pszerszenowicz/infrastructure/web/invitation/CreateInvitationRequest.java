package pszerszenowicz.infrastructure.web.invitation;

import pszerszenowicz.application.invitation.ColorChoice;

import java.util.UUID;

public record CreateInvitationRequest(UUID toUserId, ColorChoice colorChoice) {
}
