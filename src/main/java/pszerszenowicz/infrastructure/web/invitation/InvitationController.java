package pszerszenowicz.infrastructure.web.invitation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pszerszenowicz.application.invitation.InvitationId;
import pszerszenowicz.application.invitation.InvitationResult;
import pszerszenowicz.application.invitation.InvitationService;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.infrastructure.security.CurrentUserProvider;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
class InvitationController {

    private final InvitationService invitationService;
    private final CurrentUserProvider currentUser;

    InvitationController(InvitationService service,
                         CurrentUserProvider currentUser) {
        this.invitationService = service;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<CreateInvitationResponse> invite(
            @RequestBody CreateInvitationRequest req
    ) {
        InvitationId id = invitationService.invite(
                currentUser.get(),
                UserId.of(req.toUserId()),
                req.colorChoice()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateInvitationResponse(id.id()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<AcceptInvitationResponse> accept(@PathVariable UUID id) {
        GameId gameId = invitationService.accept(
                InvitationId.of(id),
                currentUser.get()
        );

        return ResponseEntity.ok(
                new AcceptInvitationResponse(gameId.id())
        );
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        invitationService.reject(
                InvitationId.of(id),
                currentUser.get()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<InvitationResult> receivedInvitations() {
        return invitationService.receivedInvitations(currentUser.get());
    }
}