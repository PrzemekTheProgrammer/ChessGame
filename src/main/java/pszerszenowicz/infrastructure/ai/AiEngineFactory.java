package pszerszenowicz.infrastructure.ai;

import org.springframework.stereotype.Component;
import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.AiType;

import java.util.Map;

@Component
public class AiEngineFactory {

    private final Map<String, AiEngine> engines;

    public AiEngineFactory(Map<String, AiEngine> engines) {
        this.engines = engines;
    }

    public AiEngine getEngine(AiType aiType) {
        return switch (aiType) {
            case HEURISTIC -> engines.get("heuristicAiEngine");
            case NNUE -> null; //TODO
        };
    }
}
