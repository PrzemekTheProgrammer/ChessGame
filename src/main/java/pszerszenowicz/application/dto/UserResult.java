package pszerszenowicz.application.dto;

import java.util.UUID;

public record UserResult(
        UUID id,
        String username
) {
}