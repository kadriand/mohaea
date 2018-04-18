package com.co.evolution.fitness.fnds;

import com.co.evolution.model.individual.Individual;

import java.util.List;

public class SPEA2FastNonDominatedSorting<T extends Individual> extends FastNonDominatedSorting<T> {

    public SPEA2FastNonDominatedSorting(List<T> population, int objectivesSize) {
        super(population, objectivesSize);
    }

    public void compareExternalWithPopulation(T comparisonIndividual) {
        double[] comparisonObjectives = comparisonIndividual.getObjectiveValues();
        int howManyDominateMe = 0;
        int until = population.size();
        for (int j = 0; j < until; ++j) {
            T loopIndividual = population.get(j);
            int comp = dominanceComparison(comparisonObjectives, loopIndividual.getObjectiveValues());
            if (comp == 1)
                howManyDominateMe++;
            double distance = euclideanDistance(comparisonIndividual, loopIndividual);
            comparisonIndividual.getDiversityMeasures().add(distance);
        }
        comparisonIndividual.setHowManyDominateMe(howManyDominateMe);
    }

    @Override
    protected void comparePointWithPopulation(int index, int from) {
        T comparisonIndividual = population.get(index);
        double[] comparisonObjectives = comparisonIndividual.getObjectiveValues();

        int until = population.size();
        for (int j = from; j < until; ++j) {
            T loopIndividual = population.get(j);
            int comp = dominanceComparison(comparisonObjectives, loopIndividual.getObjectiveValues());

            if (comp == -1)
                pushToDominateList(index, j);
            else if (comp == +1)
                pushToDominateList(j, index);

            double distance = euclideanDistance(comparisonIndividual, loopIndividual);
            comparisonIndividual.getDiversityMeasures().add(distance);
            loopIndividual.getDiversityMeasures().add(distance);
        }
        population.get(index).setHowManyDominateMe(howManyDominateMe[index]);
    }

    private double euclideanDistance(T one, T other) {
        double sum = 0;
        for (int i = 0; i < one.getObjectiveValues().length; i++)
            sum += Math.pow(one.getObjectiveValues()[i] - other.getObjectiveValues()[i], 2);
        return Math.pow(sum, 0.5);
    }

}
