package com.co.evolution.fitness.fnds;

import com.co.evolution.model.individual.Individual;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NSGA2FastNonDominatedSorting<T extends Individual> extends FastNonDominatedSorting<T> {

    private List<T>[] individualRanks;
    private double[][] objectivesRange;

    public NSGA2FastNonDominatedSorting(List<T> population, int objectivesSize) {
        super(population, objectivesSize);
    }

    @Override
    public void sort() {
        super.sort();
        this.individualRanks = new List[ranksSize + 1];
        this.objectivesRange = new double[ranksSize + 1][objectivesSize];
        for (int rank = 0; rank <= ranksSize; rank++) {
            final int rankFinal = rank;
            List<T> rankIndividuals = population.stream().filter(t -> t.getParetoRank() == rankFinal).collect(Collectors.toList());
            individualRanks[rank] = rankIndividuals;
            fillDiversityMeasures(rankIndividuals, rank);
        }
    }

    private void fillDiversityMeasures(List<T> rankIndividuals, int rank) {
        int individualsSize = rankIndividuals.size();
        for (T individual : rankIndividuals)
            individual.setDiversityMeasures(new double[individual.getObjectiveValues().length]);
        for (int o = 0; o < objectivesSize; o++) {
            final int objectiveIndex = o;
            rankIndividuals.sort(Comparator.comparingDouble(individual -> individual.getObjectiveValues()[objectiveIndex]));
            objectivesRange[rank][o] = individualsSize == 1 ? 0.0 : rankIndividuals.get(individualsSize - 1).getObjectiveValues()[o] - rankIndividuals.get(0).getObjectiveValues()[o];
            for (int i = 0; i < individualsSize; i++) {
                double objectiveCrowdingDistance;
                if (i == 0 || i == individualsSize - 1)
                    objectiveCrowdingDistance = 1.0;
                else
                    objectiveCrowdingDistance = (rankIndividuals.get(i + 1).getObjectiveValues()[o] - rankIndividuals.get(i - 1).getObjectiveValues()[o]) / objectivesRange[rank][o];
                rankIndividuals.get(i).getDiversityMeasures()[o] = objectiveCrowdingDistance;
            }
        }
    }

    @Override
    protected void comparePointWithPopulation(int index, int from) {
        super.comparePointWithPopulation(index, from);
        T individual = population.get(index);
        individual.setDiversityMeasures(new double[individual.getObjectiveValues().length]);
    }

}
