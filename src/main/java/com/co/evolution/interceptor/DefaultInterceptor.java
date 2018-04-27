package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;
import lombok.Setter;

import java.util.Arrays;

public class DefaultInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    @Setter
    private int generationsGap = 1;

    @Override
    public void apply(int generation, Population<T> population) {
        if (generation % generationsGap == 0 || generation == 1)
            System.out.println("Value: " + population.getBest().toString() + " Fitness: " + population.getBest().getFitness() + " Value: " + Arrays.toString(population.getBest().getObjectiveValues()));
    }

    @Override
    public void apply(Population<T> population) {
        System.out.println("Value: " + population.getBest().toString() + " Fitness: " + population.getBest().getFitness() + " Value: " + Arrays.toString(population.getBest().getObjectiveValues()));
    }
}
