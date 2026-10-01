package pszerszenowicz.infrastructure.security.jwt;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Unauthorized");
    }
}
