package pszerszenowicz.infrastructure.web.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pszerszenowicz.application.PlayerService;
import pszerszenowicz.application.dto.AuthResult;
import pszerszenowicz.application.dto.LoginCommand;
import pszerszenowicz.application.dto.RegisterCommand;
import pszerszenowicz.infrastructure.web.auth.dto.LoginRequest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PlayerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PlayerController playerController;

    @MockitoBean
    private PlayerService playerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }

    @Test
    void login_shouldReturnAuthResponse() throws Exception {
        LoginCommand cmd = new LoginCommand("user1", "pass123");
        AuthResult resp = new AuthResult("fake-jwt-token");

        when(playerService.login(Mockito.any())).thenReturn(resp);

        mockMvc.perform(post("/player/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("user1", "pass123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void register_shouldReturnAuthResponse() throws Exception {

        RegisterCommand req = new RegisterCommand("user1", "pass123");
        AuthResult resp = new AuthResult("fake-jwt-token");

        when(playerService.register(req)).thenReturn(resp);

        mockMvc.perform(post("/player/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

}
