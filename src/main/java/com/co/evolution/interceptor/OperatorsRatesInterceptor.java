package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;

import java.util.Arrays;

public class OperatorsRatesInterceptor<T extends Individual> extends EvolutionInterceptor<T> {

    @Override
    protected void apply(Population<T> population, int generation) {
        System.out.println("Best Individual: " + population.getBest().toString() + " Best Fitness: " + population.getBest().getFitness() + " Best Objective Functions: " + Arrays.toString(population.getBest().getObjectiveValues()));
    }
}
