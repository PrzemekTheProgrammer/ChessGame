package pszerszenowicz.infrastructure.web.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.player.BotPlayer;
import pszerszenowicz.application.player.HumanPlayer;
import pszerszenowicz.application.ports.user.UserRepository;
import pszerszenowicz.domain.ports.game.Player;

@Component
public class PlayerNameMapper {

    private final UserRepository userRepository;

    public PlayerNameMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String map(Player player) {
        if (player instanceof HumanPlayer human) {
            return userRepository.findById(human.getUserId().uuid())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User not found"
                    ))
                    .getUserName();
        }

        if (player instanceof BotPlayer) {
            return "Komputer";
        }

        throw new IllegalArgumentException(
                "Unknown player type: " + player.getClass()
        );
    }
}