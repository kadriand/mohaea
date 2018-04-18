package com.co.evolution.fitness;

import com.co.evolution.fitness.fnds.SPEA2FastNonDominatedSorting;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

public class SPEA2FitnessCalculation<T extends Individual> implements FitnessCalculation<T> {

    private SPEA2FastNonDominatedSorting<T> paretoRanks;
    private int kthNeighbor;

    @Getter
    protected ObjectiveFunction<T>[] objectiveFunctions;

    public SPEA2FitnessCalculation(ObjectiveFunction<T>[] objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    /**
     * SPEA2: Improving the Strength Pareto Evolutionary Algorithm
     */
    @Override
    public double calculate(T individual, List<T> population) {
        if (!population.contains(individual))
            paretoRanks.compareExternalWithPopulation(individual);

        individual.getDiversityMeasures().sort(Comparator.comparingDouble(Double::doubleValue));
        double sigmaSquare = individual.getDiversityMeasures().get(kthNeighbor);
        double density = 1.0 / (sigmaSquare + 2.0);

        return individual.getHowManyDominateMe() + density;
    }

    @Override
    public void newGenerationApply(List<T> population) {
        this.paretoRanks = new SPEA2FastNonDominatedSorting<>(population, this.objectiveFunctions.length);
        this.kthNeighbor = (int) Math.pow(population.size(), 0.5);
        this.paretoRanks.sort();
    }
}
