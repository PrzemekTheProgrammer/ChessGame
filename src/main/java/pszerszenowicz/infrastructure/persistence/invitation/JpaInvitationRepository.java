package pszerszenowicz.infrastructure.persistence.invitation;

import org.springframework.stereotype.Repository;
import pszerszenowicz.application.invitation.GameInvitation;
import pszerszenowicz.application.invitation.InvitationId;
import pszerszenowicz.application.ports.invitation.InvitationRepository;
import pszerszenowicz.domain.core.user.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaInvitationRepository implements InvitationRepository {

    private final SpringDataInvitationJpaRepository jpa;
    private final InvitationMapper mapper;

    public JpaInvitationRepository(SpringDataInvitationJpaRepository jpa, InvitationMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public void save(GameInvitation invitation) {
        jpa.save(mapper.toEntity(invitation));
    }

    @Override
    public Optional<GameInvitation> findById(InvitationId id) {
        return jpa.findById(id.id())
                .map(mapper::toModel);
    }

    @Override
    public List<GameInvitation> findByUser(UserId userId) {
        return jpa.findByFromOrTo(userId.uuid(), userId.uuid())
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(InvitationId id) {
        jpa.deleteById(id.id());
    }
}
