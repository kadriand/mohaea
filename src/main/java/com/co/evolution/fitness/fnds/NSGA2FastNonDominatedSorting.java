package com.co.evolution.fitness.fnds;

import com.co.evolution.model.individual.Individual;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NSGA2FastNonDominatedSorting<T extends Individual> extends FastNonDominatedSorting<T> {

    private List<T>[] ranksIndividuals;
    private double[][] objectivesRange;

    public NSGA2FastNonDominatedSorting(List<T> population, int objectivesSize) {
        super(population, objectivesSize);
    }

    @Override
    public void sort() {
        super.sort();
        this.ranksIndividuals = new List[ranksSize + 1];
        this.objectivesRange = new double[ranksSize + 1][objectivesSize];
        for (int rank = 0; rank <= ranksSize; rank++) {
            final int rankFinal = rank;
            List<T> rankIndividuals = population.stream().filter(t -> t.getParetoRank() == rankFinal).collect(Collectors.toList());
            ranksIndividuals[rank] = rankIndividuals;
            fillDiversityMeasures(rankIndividuals, rank);
        }
    }

    private void fillDiversityMeasures(List<T> rankIndividuals, int rank) {
        int individualsSize = rankIndividuals.size();
        for (int o = 0; o < objectivesSize; o++) {
            final int objectiveIndex = o;
            rankIndividuals.sort(Comparator.comparingDouble(individual -> individual.getObjectiveValues()[objectiveIndex]));
            objectivesRange[rank][o] = individualsSize == 1 ? 0.0 : rankIndividuals.get(individualsSize - 1).getObjectiveValues()[o] - rankIndividuals.get(0).getObjectiveValues()[o];
            for (int i = 0; i < individualsSize; i++) {
                double objectiveCrowdingDistance;
                if (i == 0)
                    objectiveCrowdingDistance = individualsSize == 1 ? 1.0 :(rankIndividuals.get(i + 1).getObjectiveValues()[o] - rankIndividuals.get(i).getObjectiveValues()[o]) / objectivesRange[rank][o];
                else if (i == individualsSize - 1)
                    objectiveCrowdingDistance = (rankIndividuals.get(i).getObjectiveValues()[o] - rankIndividuals.get(i - 1).getObjectiveValues()[o]) / objectivesRange[rank][o];
                else
                    objectiveCrowdingDistance = (rankIndividuals.get(i + 1).getObjectiveValues()[o] - rankIndividuals.get(i - 1).getObjectiveValues()[o]) / objectivesRange[rank][o] / 2;
                rankIndividuals.get(i).getDiversityMeasures().add(objectiveCrowdingDistance);
            }
        }
    }

    public void compareExternalWithPopulation(T comparisonIndividual) {
        double[] comparisonObjectives = comparisonIndividual.getObjectiveValues();
        int rank = 0;
        boolean dominated = true;
        while (rank < ranksSize && dominated)
            dominated = ranksIndividuals[rank++].stream().anyMatch(rankIndividual -> dominanceComparison(comparisonObjectives, rankIndividual.getObjectiveValues()) == 1);

        comparisonIndividual.setParetoRank(rank);
        List<T> rankIndividuals = ranksIndividuals[rank];
        Individual[][] closerDistances = new Individual[objectivesSize][2];

        for (T rankIndividual : rankIndividuals)
            for (int o = 0; o < objectivesSize; o++) {
                if (rankIndividual.getObjectiveValues()[o] < comparisonIndividual.getObjectiveValues()[o]) {
                    if (closerDistances[o][0] == null || comparisonIndividual.getObjectiveValues()[o] - rankIndividual.getObjectiveValues()[o] < closerDistances[o][0].getObjectiveValues()[o])
                        closerDistances[o][0] = rankIndividual;
                } else if (closerDistances[o][1] == null || rankIndividual.getObjectiveValues()[o] - comparisonIndividual.getObjectiveValues()[o] < closerDistances[o][1].getObjectiveValues()[o])
                    closerDistances[o][1] = rankIndividual;
            }

        for (int o = 0; o < objectivesSize; o++) {
            double objectiveCrowdingDistance;
            if (closerDistances[o][0] == null)
                objectiveCrowdingDistance = (closerDistances[o][1].getObjectiveValues()[o] - comparisonIndividual.getObjectiveValues()[o]) / (objectivesRange[rank][o] + (closerDistances[o][1].getObjectiveValues()[o] - comparisonIndividual.getObjectiveValues()[o]));
            else if (closerDistances[o][1] == null)
                objectiveCrowdingDistance = (comparisonIndividual.getObjectiveValues()[o] - closerDistances[o][0].getObjectiveValues()[o]) / (objectivesRange[rank][o] + (comparisonIndividual.getObjectiveValues()[o] - closerDistances[o][0].getObjectiveValues()[o]));
            else
                objectiveCrowdingDistance = (closerDistances[o][1].getObjectiveValues()[o] - closerDistances[o][0].getObjectiveValues()[o]) / objectivesRange[rank][o];
            comparisonIndividual.getDiversityMeasures().add(objectiveCrowdingDistance);
        }
    }

    @Override
    protected void comparePointWithPopulation(int index, int from) {
        super.comparePointWithPopulation(index, from);
        population.get(index).getDiversityMeasures().clear();
    }

}
