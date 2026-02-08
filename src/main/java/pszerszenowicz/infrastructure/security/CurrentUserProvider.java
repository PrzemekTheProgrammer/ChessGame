package pszerszenowicz.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.infrastructure.security.jwt.UnauthorizedException;

import java.util.UUID;

@Component
public class CurrentUserProvider {

    public UserId get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException();
        }
        return UserId.of(UUID.fromString(auth.getName()));
    }

}
