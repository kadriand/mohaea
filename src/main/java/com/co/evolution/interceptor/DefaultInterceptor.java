package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;

import java.util.Arrays;
import java.util.Map;

public class DefaultInterceptor<T extends Individual> extends EvolutionInterceptor<T> {

    @Override
    protected void apply(Population<T> population, int generation, Map<T, double[]> operatorsRates) {
        System.out.println("Generation: " + generation + ". Best Individual: " + population.getBest().toString() + " Best Fitness: " + population.getBest().getFitness() + " Best Objective Functions: " + Arrays.toString(population.getBest().getObjectiveValues()));
    }
}
