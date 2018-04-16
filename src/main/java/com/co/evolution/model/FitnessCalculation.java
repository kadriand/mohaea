package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

import java.util.List;

public interface FitnessCalculation<T extends Individual> {


    ObjectiveFunction<T>[] getObjectiveFunctions();

    double calculate(T individual, List<T> population);

    void newGenerationApply(List<T> population);

}
