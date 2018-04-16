package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

import java.util.List;

public interface EvolutionInterceptor<T extends Individual> {

    void apply(int generation, List<T> population);

}
