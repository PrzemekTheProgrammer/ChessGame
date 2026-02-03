package pszerszenowicz.application.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.ports.game.GameCreator;
import pszerszenowicz.application.ports.game.GameFactory;
import pszerszenowicz.domain.core.game.GameType;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DefaultGameFactory implements GameFactory {

    private final Map<GameType, GameCreator> creators;

    DefaultGameFactory(Set<GameCreator> creators) {
        this.creators = creators.stream()
                .collect(Collectors.toMap(
                        GameCreator::gameType,
                        c -> c
                ));
    }

    @Override
    public Game create(GameType gameType, Player p1, Player p2) {
        return creators.get(gameType).create(p1,p2);
    }
}
