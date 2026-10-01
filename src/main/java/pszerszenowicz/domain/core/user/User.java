package pszerszenowicz.domain.core.user;

import org.springframework.security.crypto.password.PasswordEncoder;

public class User {
    private final UserId id;
    private String userName;
    private String passwordHash;

    public static User register(
            String username,
            String rawPassword,
            PasswordEncoder encoder
    ) {
        return new User(username, rawPassword, encoder);
    }

    public static User restore(
            UserId id,
            String username,
            String passwordHash
    ) {
        return new User(id, username, passwordHash);
    }

    protected User(String userName, String password, PasswordEncoder encoder) {
        this.id = UserId.random();
        this.userName = userName;
        this.passwordHash = encoder.encode(password);
    }

    protected User(UserId id, String userName, String passwordHash) {
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public UserId getId() {
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
