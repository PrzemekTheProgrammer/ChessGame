package pszerszenowicz.infrastructure.persistence.invitation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataInvitationJpaRepository
        extends JpaRepository<InvitationEntity, UUID> {

    List<InvitationEntity> findByFromOrTo(UUID from, UUID to);

    List<InvitationEntity> findByTo(UUID to);
}