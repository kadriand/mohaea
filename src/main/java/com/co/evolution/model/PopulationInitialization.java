package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

public interface PopulationInitialization<T extends Individual> {

    Population<T> init(FitnessCalculation<T> fitnessCalculation);

    T initRandom();

}
