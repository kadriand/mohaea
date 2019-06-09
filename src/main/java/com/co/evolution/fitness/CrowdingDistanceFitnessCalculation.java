package com.co.evolution.fitness;

import com.co.evolution.fitness.fnds.NSGA2FastNonDominatedSorting;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Penalization;
import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.List;
import java.util.stream.DoubleStream;

public class CrowdingDistanceFitnessCalculation<T extends Individual> implements FitnessCalculation<T> {

    private NSGA2FastNonDominatedSorting<T> paretoRanks;

    @Getter
    protected ObjectiveFunction<T>[] objectiveFunctions;
    protected Penalization<T> penalization;

    public CrowdingDistanceFitnessCalculation(ObjectiveFunction<T>... objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    public CrowdingDistanceFitnessCalculation(Penalization<T> penalization, ObjectiveFunction<T>... objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
        this.penalization = penalization;
    }

    @Override
    public double[] computeObjectives(T individual) {
        double[] objectiveValues = new double[objectiveFunctions.length];
        for (int i = 0; i < objectiveFunctions.length; i++)
            objectiveValues[i] = objectiveFunctions[i].apply(individual);
        return objectiveValues;
    }

    /**
     * NSGA2: Improving the Strength Pareto Evolutionary Algorithm
     */
    @Override
    public double computeIndividualFitness(T individual, List<T> population) {
        if (!population.contains(individual))
            paretoRanks.fillExternalIndividualDiversityMeasures(individual);

        double crowdingDistance = DoubleStream.of(individual.getDiversityMeasures()).sum();
        double density = 1.0 / (2.0 + crowdingDistance);
        if (density > 1)
            System.out.println(density);
        double individualPenalization = this.penalization == null ? 0.0 : this.penalization.apply(individual);
        return individual.getParetoRank() + density + individualPenalization;
    }

    @Override
    public void computePopulationRanksFitness(List<T> population) {
        this.paretoRanks = new NSGA2FastNonDominatedSorting<>(population, this.objectiveFunctions.length);
        this.paretoRanks.sort();
        for (T individual : population)
            individual.setFitness(computeIndividualFitness(individual, population));
    }
}
