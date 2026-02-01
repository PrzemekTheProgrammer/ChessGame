package pszerszenowicz.domain.core.player;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Player {
    private final PlayerId id;
    private String userName;
    private String passwordHash;

    public static Player register(
            String username,
            String rawPassword,
            PasswordEncoder encoder
    ) {
        return new Player(username, rawPassword, encoder);
    }

    public static Player restore(
            PlayerId id,
            String username,
            String passwordHash
    ) {
        return new Player(id, username, passwordHash);
    }

    protected Player(String userName, String password, PasswordEncoder encoder) {
        this.id = PlayerId.random();
        this.userName = userName;
        this.passwordHash = encoder.encode(password);
    }

    protected Player(PlayerId id, String userName, String passwordHash) {
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public PlayerId getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public boolean passwordMatches(String password, PasswordEncoder encoder) {
        return encoder.matches(password, passwordHash);
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
