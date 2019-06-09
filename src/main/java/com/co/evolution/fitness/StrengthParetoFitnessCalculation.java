package com.co.evolution.fitness;

import com.co.evolution.fitness.fnds.SPEA2FastNonDominatedSorting;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Penalization;
import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class StrengthParetoFitnessCalculation<T extends Individual> implements FitnessCalculation<T> {

    private SPEA2FastNonDominatedSorting<T> paretoRanks;
    private int kthNeighbor;

    @Getter
    protected ObjectiveFunction<T>[] objectiveFunctions;
    protected Penalization<T> penalization;

    public StrengthParetoFitnessCalculation(ObjectiveFunction<T>... objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    public StrengthParetoFitnessCalculation(Penalization<T> penalization, ObjectiveFunction<T>... objectiveFunctions) {
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
     * SPEA2: Improving the Strength Pareto Evolutionary Algorithm
     */
    @Override
    public double computeIndividualFitness(T individual, List<T> population) {
        if (!population.contains(individual))
            paretoRanks.fillExternalIndividualDiversityMeasures(individual);

        Arrays.sort(individual.getDiversityMeasures());
        double sigmaSquare = individual.getDiversityMeasures()[kthNeighbor];
        double density = 1.0 / (sigmaSquare + 2.0);

        double individualPenalization = this.penalization == null ? 0.0 : this.penalization.apply(individual);
        return individual.getHowManyDominateMe() + density + individualPenalization * kthNeighbor;
    }

    @Override
    public void computePopulationRanksFitness(List<T> population) {
        this.paretoRanks = new SPEA2FastNonDominatedSorting<>(population, this.objectiveFunctions.length);
        this.kthNeighbor = (int) Math.pow(population.size(), 0.5);
        for (T individual : population)
            individual.setDiversityMeasures(new double[population.size() - 1]);

        this.paretoRanks.sort();
        for (T individual : population)
            individual.setFitness(computeIndividualFitness(individual, population));
    }
}
