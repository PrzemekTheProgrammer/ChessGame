package pszerszenowicz.infrastructure.web.invitation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pszerszenowicz.application.invitation.InvitationId;
import pszerszenowicz.application.invitation.InvitationService;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.infrastructure.security.CurrentUserProvider;

import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
class InvitationController {

    private final InvitationService service;
    private final CurrentUserProvider currentUser;

    InvitationController(InvitationService service,
                         CurrentUserProvider currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<CreateInvitationResponse> invite(
            @RequestBody CreateInvitationRequest req
    ) {
        InvitationId id = service.invite(
                currentUser.get(),
                UserId.of(req.toUserId()),
                req.colorChoice()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateInvitationResponse(id.id()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<CreateInvitationResponse> accept(@PathVariable UUID id) {
        GameId gameId = service.accept(
                InvitationId.of(id),
                currentUser.get()
        );

        return ResponseEntity.ok(new CreateInvitationResponse(gameId.id()));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        service.reject(
                InvitationId.of(id),
                currentUser.get()
        );

        return ResponseEntity.noContent().build();
    }
}