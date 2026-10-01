package pszerszenowicz.infrastructure.persistence.user;

import org.springframework.stereotype.Component;
import pszerszenowicz.domain.core.user.User;
import pszerszenowicz.domain.core.user.UserId;


@Component
class UserMapper {

    public User toDomain(UserEntity e) {
        return User.restore(
                UserId.of(e.getId()),
                e.getUsername(),
                e.getPasswordHash()
        );
    }

    public UserEntity toEntity(User p) {
        UserEntity e = new UserEntity();
        e.setId(p.getId().uuid());
        e.setUsername(p.getUserName());
        e.setPasswordHash(p.getPasswordHash());
        return e;
    }
}
