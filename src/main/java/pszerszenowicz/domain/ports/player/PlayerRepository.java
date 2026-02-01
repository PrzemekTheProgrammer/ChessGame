package pszerszenowicz.domain.ports.player;

import pszerszenowicz.domain.core.player.Player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {
    Optional<Player> findByUsername(String username);
    Optional<Player> findById(UUID id);
    Player save(Player player);
}
