package pszerszenowicz.infrastructure.persistence.invitation;

import jakarta.persistence.*;
import pszerszenowicz.application.invitation.ColorChoice;
import pszerszenowicz.application.invitation.InvitationStatus;

import java.util.UUID;

@Entity
@Table(name = "invitations")
class InvitationEntity {

    @Id
    private UUID uuid;
    @Column(name = "from_user", nullable = false)
    private  UUID from;
    @Column(name = "to_user", nullable = false)
    private  UUID to;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  ColorChoice colorChoice;

    protected InvitationEntity(){}

    protected InvitationEntity(UUID uuid, UUID from, UUID to, InvitationStatus status, ColorChoice colorChoice) {
        this.uuid = uuid;
        this.from = from;
        this.to = to;
        this.status = status;
        this.colorChoice = colorChoice;
    }

    public static InvitationEntity create(
            UUID id,
            UUID from,
            UUID to,
            ColorChoice color
    ) {
        return new InvitationEntity(
                id,
                from,
                to,
                InvitationStatus.PENDING,
                color
        );
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getFrom() {
        return from;
    }

    public void setFrom(UUID from) {
        this.from = from;
    }

    public UUID getTo() {
        return to;
    }

    public void setTo(UUID to) {
        this.to = to;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public ColorChoice getColorChoice() {
        return colorChoice;
    }

    public void setColorChoice(ColorChoice colorChoice) {
        this.colorChoice = colorChoice;
    }
}
