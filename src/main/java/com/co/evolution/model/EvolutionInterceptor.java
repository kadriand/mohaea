package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

public interface EvolutionInterceptor<T extends Individual> {

    void apply(int generation, Population<T> population);

    void apply(Population<T> population);

}
