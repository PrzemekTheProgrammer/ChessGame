package pszerszenowicz.infrastructure.web.invitation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pszerszenowicz.application.invitation.InvitationId;
import pszerszenowicz.application.invitation.InvitationService;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.infrastructure.security.CurrentUserProvider;
import pszerszenowicz.infrastructure.security.jwt.JwtAuthenticationFilter;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InvitationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        ))
@AutoConfigureMockMvc(addFilters = false)
class InvitationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InvitationService service;

    @MockitoBean
    private CurrentUserProvider currentUser;

    private final UserId user = UserId.random();

    @Test
    void should_create_invitation() throws Exception {
        UUID to = UUID.randomUUID();
        InvitationId id = InvitationId.random();

        when(currentUser.get()).thenReturn(user);
        when(service.invite(any(), any(), any()))
                .thenReturn(id);

        mockMvc.perform(post("/api/invitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "toUserId": "%s",
                                  "gameType": "CHESS",
                                  "colorChoice": "RANDOM"
                                }
                                """.formatted(to)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invitationId").value(id.id().toString()));
    }

    @Test
    void should_accept_invitation() throws Exception {
        UUID invId = UUID.randomUUID();
        GameId gameId = GameId.random();

        when(currentUser.get()).thenReturn(user);
        when(service.accept(any(), any()))
                .thenReturn(gameId);

        mockMvc.perform(post("/api/invitations/{id}/accept", invId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invitationId").value(gameId.id().toString()));
    }

    @Test
    void should_reject_invitation() throws Exception {
        UUID invId = UUID.randomUUID();

        when(currentUser.get()).thenReturn(user);

        mockMvc.perform(post("/api/invitations/{id}/reject", invId))
                .andExpect(status().isNoContent());

        verify(service).reject(any(), any());
    }
}
