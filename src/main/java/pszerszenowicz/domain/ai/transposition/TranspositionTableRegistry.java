package pszerszenowicz.domain.ai.transposition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TranspositionTableRegistry {


    private static final int DEFAULT_TT_SIZE = 1 << 20;

    private final Map<EvaluatorId, TranspositionTable> tables =
            new ConcurrentHashMap<>();

    public TranspositionTable get(EvaluatorId evaluatorId) {
        return tables.computeIfAbsent(
                evaluatorId,
                key -> new TranspositionTable(DEFAULT_TT_SIZE));
    }
}
