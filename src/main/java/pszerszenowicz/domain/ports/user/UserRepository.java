package pszerszenowicz.domain.ports.user;

import pszerszenowicz.domain.core.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findById(UUID id);
    User save(User player);
}
