package pszerszenowicz.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.HeuristicsEvaluator;

@Configuration
public class AiConfig {
    @Bean
    public HeuristicsEvaluator heuristicsEvaluator() {
        return new HeuristicsEvaluator();
    }

    @Bean("heuristicAiEngine")
    public AiEngine heuristicEngine(HeuristicsEvaluator heuristicsEvaluator) {
        return new AiEngine(heuristicsEvaluator);
    }
}
