package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class Algorithm<T extends Individual> {

    protected boolean minimize;
    protected List<GeneticOperator<T>> geneticOperators;
    protected TerminationCondition<T> terminationCondition;
    protected SelectionMethod<T> selectionMethod;
    protected PopulationInitialization<T> initialization;
    protected FitnessCalculation<T> fitnessCalculation;
    protected EvolutionInterceptor<T> evolutionInterceptor;

    public abstract List<T> apply();

    public T getBest(List<T> population)
    {
        T best = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            T actual = population.get(i);
            if (minimize && actual.compareTo(best) < 0)
                best = actual;
            else if (!minimize && actual.compareTo(best) > 0)
                best = actual;
        }
        return best;
    }


}
