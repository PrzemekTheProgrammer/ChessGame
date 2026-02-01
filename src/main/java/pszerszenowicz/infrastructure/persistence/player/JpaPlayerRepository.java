package pszerszenowicz.infrastructure.persistence.player;

import org.springframework.stereotype.Repository;
import pszerszenowicz.domain.core.player.Player;
import pszerszenowicz.domain.ports.player.PlayerRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaPlayerRepository implements PlayerRepository {

    private final SpringDataPlayerJpaRepository jpa;
    private final PlayerMapper mapper;

    public JpaPlayerRepository(SpringDataPlayerJpaRepository jpa,  PlayerMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return jpa.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Player save(Player player) {
        PlayerEntity entity = mapper.toEntity(player);
        return mapper.toDomain(jpa.save(entity));
    }
}
