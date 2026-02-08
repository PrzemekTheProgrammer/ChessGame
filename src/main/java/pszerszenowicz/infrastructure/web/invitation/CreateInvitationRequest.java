package pszerszenowicz.infrastructure.web.invitation;

import pszerszenowicz.application.invitation.ColorChoice;
import pszerszenowicz.domain.core.game.GameType;

import java.util.UUID;

public record CreateInvitationRequest(UUID toUserId,
                                      GameType gameType,
                                      ColorChoice colorChoice) {
}
