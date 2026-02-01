package pszerszenowicz.infrastructure.persistence.player;

import org.springframework.stereotype.Component;
import pszerszenowicz.domain.core.player.Player;
import pszerszenowicz.domain.core.player.PlayerId;


@Component
public class PlayerMapper {

    public Player toDomain(PlayerEntity e) {
        return Player.restore(
                PlayerId.of(e.getId()),
                e.getUsername(),
                e.getPasswordHash()
        );
    }

    public PlayerEntity toEntity(Player p) {
        PlayerEntity e = new PlayerEntity();
        e.setId(p.getId().uuid());
        e.setUsername(p.getUserName());
        e.setPasswordHash(p.getPasswordHash());
        return e;
    }
}
