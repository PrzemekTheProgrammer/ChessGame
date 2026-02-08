package pszerszenowicz.infrastructure.persistence.invitation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataInvitationJpaRepository extends JpaRepository<InvitationEntity, UUID> {
    Optional<InvitationEntity> findByFromOrTo(UUID from, UUID to);
}
