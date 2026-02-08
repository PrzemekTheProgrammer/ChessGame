package pszerszenowicz.application.ports.invitation;

import pszerszenowicz.application.invitation.GameInvitation;
import pszerszenowicz.application.invitation.InvitationId;
import pszerszenowicz.domain.core.user.UserId;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository {
    void save(GameInvitation invitation);

    Optional<GameInvitation> findById(InvitationId id);

    List<GameInvitation> findByUser(UserId userId);

    void delete(InvitationId id);
}
