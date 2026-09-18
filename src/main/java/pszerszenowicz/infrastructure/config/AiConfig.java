package pszerszenowicz.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pszerszenowicz.domain.ai.AiEngine;
import pszerszenowicz.domain.ai.HeuristicsEvaluator;
import pszerszenowicz.domain.ai.transposition.TranspositionTableRegistry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AiConfig {
    @Bean
    public HeuristicsEvaluator heuristicsEvaluator() {
        return new HeuristicsEvaluator();
    }

    @Bean
    public TranspositionTableRegistry transpositionTableRegistry() {
        return new TranspositionTableRegistry();
    }

    @Bean
    public Executor aiExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean("heuristicAiEngine")
    public AiEngine heuristicAiEngine(
            HeuristicsEvaluator evaluator,
            TranspositionTableRegistry ttRegistry
    ) {
        return new AiEngine(
                evaluator,
                ttRegistry.get(evaluator.getId())
        );
    }
}
