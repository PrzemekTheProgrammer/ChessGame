package pszerszenowicz.infrastructure.persistence.invitation;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.invitation.GameInvitation;
import pszerszenowicz.application.invitation.InvitationId;
import pszerszenowicz.domain.core.user.UserId;

@Component
class InvitationMapper {

    public GameInvitation toModel(InvitationEntity inv) {
        return GameInvitation.restore(InvitationId.of(inv.getUuid()),
                UserId.of(inv.getFrom()),
                UserId.of(inv.getTo()),
                inv.getStatus(),
                inv.getColorChoice());
    }

    public InvitationEntity toEntity(GameInvitation inv) {
        InvitationEntity e = new InvitationEntity();
        e.setUuid(inv.getId().id());
        e.setFrom(inv.getFrom().uuid());
        e.setTo(inv.getTo().uuid());
        e.setStatus(inv.getStatus());
        e.setColorChoice(inv.getColorChoice());
        return e;
    }

}
