package com.co.evolution.fitness;

import com.co.evolution.fitness.fnds.NSGA2FastNonDominatedSorting;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.List;

public class NSGA2FitnessCalculation<T extends Individual> implements FitnessCalculation<T> {

    private NSGA2FastNonDominatedSorting<T> paretoRanks;

    @Getter
    protected ObjectiveFunction<T>[] objectiveFunctions;

    public NSGA2FitnessCalculation(ObjectiveFunction<T>[] objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    /**
     * NSGA2: Improving the Strength Pareto Evolutionary Algorithm
     */
    @Override
    public double calculate(T individual, List<T> population) {
        if (!population.contains(individual))
            paretoRanks.compareExternalWithPopulation(individual);
        double crowdingDistance = 0.0;
        for (int o = 0; o < objectiveFunctions.length; o++)
            crowdingDistance += individual.getDiversityMeasures().get(o);
        double density = 1.0 / (crowdingDistance + 2.0);
        if(density>1)
            System.out.println(density);
        return individual.getParetoRank() + density;
    }

    @Override
    public void newGenerationApply(List<T> population) {
        this.paretoRanks = new NSGA2FastNonDominatedSorting<>(population, this.objectiveFunctions.length);
        this.paretoRanks.sort();
    }
}
