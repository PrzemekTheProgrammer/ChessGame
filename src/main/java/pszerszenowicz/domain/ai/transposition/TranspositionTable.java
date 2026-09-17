package pszerszenowicz.domain.ai.transposition;

import java.util.Arrays;

public class TranspositionTable {

    private final TranspositionEntry[] entries;

    public TranspositionTable(int size) {
        if (size <= 0 || Integer.bitCount(size) != 1) {
            throw new IllegalArgumentException(
                    "Transposition table size must be a positive power of two"
            );
        }

        this.entries = new TranspositionEntry[size];
    }

    public TranspositionEntry get(long hash) {
        int index = index(hash);

        TranspositionEntry entry = entries[index];

        if (entry == null || entry.hash() != hash) {
            return null;
        }

        return entry;
    }

    public void put(long hash, TranspositionEntry newEntry) {
        int index = index(hash);

        TranspositionEntry oldEntry = entries[index];

        if (oldEntry == null || newEntry.depth() >= oldEntry.depth()) {
            entries[index] = newEntry;
        }
    }

    public void clear() {
        Arrays.fill(entries, null);
    }

    public int size() {
        int count = 0;

        for (TranspositionEntry entry : entries) {
            if (entry != null) {
                count++;
            }
        }

        return count;
    }

    private int index(long hash) {
        return (int) hash & (entries.length - 1);
    }
}