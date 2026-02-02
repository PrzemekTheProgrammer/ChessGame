package pszerszenowicz.infrastructure.persistence.user;

import org.springframework.stereotype.Repository;
import pszerszenowicz.domain.core.user.User;
import pszerszenowicz.domain.ports.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserJpaRepository jpa;
    private final UserMapper mapper;

    public JpaUserRepository(SpringDataUserJpaRepository jpa, UserMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public User save(User player) {
        UserEntity entity = mapper.toEntity(player);
        return mapper.toDomain(jpa.save(entity));
    }
}
