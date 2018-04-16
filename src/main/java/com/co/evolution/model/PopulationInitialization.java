package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

import java.util.List;

public interface PopulationInitialization<T extends Individual> {

    List<T> init(FitnessCalculation<T> fitnessCalculation);

}
