package pszerszenowicz.domain.ai.transposition;

import java.util.HashMap;
import java.util.Map;

public class TranspositionTable {

    private final Map<Long, TranspositionEntry> table = new HashMap<>();

    public TranspositionEntry get(long hash) {
        return table.get(hash);
    }

    public void put(long hash, TranspositionEntry entry) {
        table.put(hash, entry);
    }

    public void clear() {
        table.clear();
    }

    public int size() {
        return table.size();
    }
}