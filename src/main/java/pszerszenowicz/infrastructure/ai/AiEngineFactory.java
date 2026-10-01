package pszerszenowicz.infrastructure.ai;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.AiType;

import java.util.Map;

@Component
public class AiEngineFactory {

    private final ObjectProvider<AiEngine> heuristicProvider;
    private final ObjectProvider<AiEngine> nnueProvider;

    public AiEngineFactory(
            @Qualifier("heuristicAiEngine")
            ObjectProvider<AiEngine> heuristicProvider,
            @Qualifier("nnueAiEngine")
            ObjectProvider<AiEngine> nnueProvider
    ) {
        this.heuristicProvider = heuristicProvider;
        this.nnueProvider = nnueProvider;
    }

    public AiEngine getEngine(AiType aiType) {
        return switch (aiType) {
            case HEURISTIC -> heuristicProvider.getObject();
            case NNUE -> nnueProvider.getObject();
        };
    }
}