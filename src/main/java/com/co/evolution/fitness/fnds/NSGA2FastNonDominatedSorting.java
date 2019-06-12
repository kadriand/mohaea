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

    public void fillExternalIndividualDiversityMeasures(T externalIndividual) {
        double[] comparisonObjectives = externalIndividual.getObjectiveValues();
        int rank = 0;
        boolean dominated = true;
        while (rank < ranksSize && dominated)
            dominated = individualRanks[rank++].stream().anyMatch(rankIndividual -> dominanceComparison(comparisonObjectives, rankIndividual.getObjectiveValues()) == 1);

        int individualRank = rank - 1;
        externalIndividual.setParetoRank(individualRank);
        List<T> rankIndividuals = individualRanks[individualRank];
        Individual[][] nearestIndividuals = new Individual[objectivesSize][2];

        for (T rankIndividual : rankIndividuals)
            for (int o = 0; o < objectivesSize; o++)
                if (rankIndividual.getObjectiveValues()[o] < externalIndividual.getObjectiveValues()[o]) {
                    if (nearestIndividuals[o][0] == null || externalIndividual.getObjectiveValues()[o] - rankIndividual.getObjectiveValues()[o] < externalIndividual.getObjectiveValues()[o] - nearestIndividuals[o][0].getObjectiveValues()[o])
                        nearestIndividuals[o][0] = rankIndividual;
                } else if (externalIndividual.getObjectiveValues()[o] < rankIndividual.getObjectiveValues()[o]) {
                    if (nearestIndividuals[o][1] == null || rankIndividual.getObjectiveValues()[o] - externalIndividual.getObjectiveValues()[o] < nearestIndividuals[o][1].getObjectiveValues()[o] - externalIndividual.getObjectiveValues()[o])
                        nearestIndividuals[o][1] = rankIndividual;
                }

        externalIndividual.setDiversityMeasures(new double[objectivesSize]);
        for (int o = 0; o < objectivesSize; o++) {
            double objectiveCrowdingDistance;
            if (nearestIndividuals[o][0] == null || nearestIndividuals[o][1] == null)
                objectiveCrowdingDistance = 1.0;
            else
                objectiveCrowdingDistance = (nearestIndividuals[o][1].getObjectiveValues()[o] - nearestIndividuals[o][0].getObjectiveValues()[o]) / objectivesRange[individualRank][o];
            externalIndividual.getDiversityMeasures()[o] = objectiveCrowdingDistance;
        }
    }

    @Override
    protected void comparePointWithPopulation(int index, int from) {
        super.comparePointWithPopulation(index, from);
        T individual = population.get(index);
        individual.setDiversityMeasures(new double[individual.getObjectiveValues().length]);
    }

}
