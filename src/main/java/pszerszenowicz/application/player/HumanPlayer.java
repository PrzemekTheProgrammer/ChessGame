package pszerszenowicz.application.player;

import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;

public class HumanPlayer implements Player {
    private final UserId id;

    public HumanPlayer(UserId id) {
        this.id = id;
    }

    public UserId getUserId() {
        return id;
    }
}
