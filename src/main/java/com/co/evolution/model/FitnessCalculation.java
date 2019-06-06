package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

import java.util.List;

public interface FitnessCalculation<T extends Individual> {

    ObjectiveFunction<T>[] getObjectiveFunctions();

    double[] computeObjectives(T individual);

    double computeIndividualFitness(T individual, List<T> population);

    void computePopulationFitness(List<T> population);

}
