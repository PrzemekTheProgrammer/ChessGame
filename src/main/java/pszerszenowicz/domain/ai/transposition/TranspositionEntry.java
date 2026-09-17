package pszerszenowicz.domain.ai.transposition;

public record TranspositionEntry(
        int depth,
        int score,
        Bound bound,
        long hash
) {
    public enum Bound {
        EXACT,
        LOWER,
        UPPER
    }
}