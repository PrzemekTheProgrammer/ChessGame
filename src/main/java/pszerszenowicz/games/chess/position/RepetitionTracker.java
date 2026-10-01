package pszerszenowicz.games.chess.position;

import java.util.HashMap;
import java.util.Map;

public class RepetitionTracker {

    private final Map<Long, Integer> occurrences;

    public RepetitionTracker() {
        this.occurrences = new HashMap<>();
    }

    public RepetitionTracker(RepetitionTracker other) {
        this.occurrences = new HashMap<>(other.occurrences);
    }

    public void add(ChessPosition position) {
        long hash = ZobristHasher.repetitionHash(position);

        occurrences.merge(
                hash,
                1,
                Integer::sum
        );
    }

    public void remove(ChessPosition position) {
        long hash = ZobristHasher.repetitionHash(position);

        occurrences.computeIfPresent(hash, (key, count) -> {
            if (count <= 1) {
                return null;
            }

            return count - 1;
        });
    }

    public int occurrences(ChessPosition position) {
        long hash = ZobristHasher.repetitionHash(position);

        return occurrences.getOrDefault(hash, 0);
    }

    public boolean isThreefoldRepetition(ChessPosition position) {
        return occurrences(position) >= 3;
    }
}