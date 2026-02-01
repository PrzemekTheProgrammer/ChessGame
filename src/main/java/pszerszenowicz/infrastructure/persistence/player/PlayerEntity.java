package pszerszenowicz.infrastructure.persistence.player;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "players",
        uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class PlayerEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    protected PlayerEntity() {}

    public PlayerEntity(UUID id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}