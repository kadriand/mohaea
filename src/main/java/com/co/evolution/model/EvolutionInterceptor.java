package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class EvolutionInterceptor<T extends Individual> {

    @Setter
    protected int generationsGap = 1;

    @Setter
    protected boolean onlyBest = false;

    protected abstract void apply(Population<T> population, int generation, Map<T, double[]> operatorsRates);

    public void intercept(Population<T> population, int generation, Map<T, double[]> operatorsRates, boolean best) {
        if (onlyBest || best)
            population = population.stream().filter(t -> t.getParetoRank() == 0).collect(Collectors.toCollection(Population::new));
        if (generation % generationsGap == 0 || generation <= 1)
            apply(population, generation, operatorsRates);
    }

}
