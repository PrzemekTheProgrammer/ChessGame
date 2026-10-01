package pszerszenowicz.application.ports.user;

import pszerszenowicz.application.dto.PageResult;
import pszerszenowicz.domain.core.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID id);

    User save(User player);

    PageResult<User> searchByUsername(String query, int page, int size);
}
