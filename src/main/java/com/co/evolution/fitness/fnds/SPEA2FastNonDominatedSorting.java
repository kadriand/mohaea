package com.co.evolution.fitness.fnds;

import com.co.evolution.model.individual.Individual;

import java.util.List;

public class SPEA2FastNonDominatedSorting<T extends Individual> extends FastNonDominatedSorting<T> {

    public SPEA2FastNonDominatedSorting(List<T> population, int objectivesSize) {
        super(population, objectivesSize);
    }

    public void fillExternalIndividualDiversityMeasures(T comparisonIndividual) {
        double[] comparisonObjectives = comparisonIndividual.getObjectiveValues();
        int howManyDominateMe = 0;
        int until = population.size();
        comparisonIndividual.setDiversityMeasures(new double[population.size()]);
        double adjustmentFactor = 1 + 1 / population.size();
        for (int j = 0; j < until; ++j) {
            T loopIndividual = population.get(j);
            int comp = dominanceComparison(comparisonObjectives, loopIndividual.getObjectiveValues());
            if (comp == 1)
                howManyDominateMe++;
            double distance = euclideanDistance(comparisonIndividual, loopIndividual);
            comparisonIndividual.getDiversityMeasures()[j] = distance*adjustmentFactor;
        }
        comparisonIndividual.setHowManyDominateMe(howManyDominateMe);
    }

    @Override
    protected void comparePointWithPopulation(int individualIdx, int from) {
        T comparisonIndividual = population.get(individualIdx);
        double[] comparisonObjectives = comparisonIndividual.getObjectiveValues();

        int populationSize = population.size();
        for (int j = from; j < populationSize; ++j) {
            T individualDominance = population.get(j);
            int comp = dominanceComparison(comparisonObjectives, individualDominance.getObjectiveValues());
            if (comp == -1)
                pushToDominateList(individualIdx, j);
            else if (comp == +1)
                pushToDominateList(j, individualIdx);

            double distance = euclideanDistance(comparisonIndividual, individualDominance);
            comparisonIndividual.getDiversityMeasures()[j - 1] = distance;
            individualDominance.getDiversityMeasures()[individualIdx] = distance;
        }
        population.get(individualIdx).setHowManyDominateMe(howManyDominateMe[individualIdx]);
    }

    private double euclideanDistance(T one, T other) {
        double sum = 0;
        for (int i = 0; i < one.getObjectiveValues().length; i++)
            sum += Math.pow(one.getObjectiveValues()[i] - other.getObjectiveValues()[i], 2);
        return Math.pow(sum, 0.5);
    }

}
