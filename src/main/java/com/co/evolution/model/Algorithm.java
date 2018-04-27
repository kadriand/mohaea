package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class Algorithm<T extends Individual> {

    protected List<GeneticOperator<T>> geneticOperators;
    protected TerminationCondition<T> terminationCondition;
    protected SelectionMethod<T> selectionMethod;
    protected PopulationInitialization<T> initialization;
    protected FitnessCalculation<T> fitnessCalculation;
    protected EvolutionInterceptor<T> evolutionInterceptor;

    public abstract Population<T> apply();

}
