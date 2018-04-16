package com.co.evolution.fitness.fnds;

import com.co.evolution.fitness.fnds.util.DominanceHelper;
import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * This is the implementation of the fast non-dominated sorting algorithm
 * following the original description, as in the following paper:
 *
 * <pre>
 * {@literal @}article{ nsga-ii,
 *     author      = {Kalyanmoy Deb and Amrit Pratap and Sameer Agarwal and T. Meyarivan},
 *     title       = {A Fast and Elitist Multi-Objective Genetic Algorithm: {NSGA}-{II}},
 *     journal     = {IEEE Transactions on Evolutionary Computation},
 *     year        = {2002},
 *     volume      = {6},
 *     number      = {2},
 *     pages       = {182-197},
 *     publisher   = {IEEE Press},
 *     langid      = {english}
 * }
 * </pre>
 * <p>
 * The running time complexity is O(N^2 M), the memory complexity is O(N^2).
 *
 * @author Kalyanmoy Deb (algorithm)
 * @author Amrit Pratap (algorithm)
 * @author Sameer Agarwal (algorithm)
 * @author T. Meyarivan (algorithm)
 * @author Maxim Buzdalov (implementation)
 */

public class FastNonDominatedSorting<T extends Individual> extends NonDominatedSorting<T> {
    private int[] queue;
    @Getter
    private int[] howManyIDominate;
    @Getter
    private int[] howManyDominateMe;
    @Getter
    private int[][] whoIDominate;

    public FastNonDominatedSorting(int maximumPoints, int maximumObjectives) {
        super(maximumPoints, maximumObjectives);
        restore();
    }

    @Override
    protected void restore() {
        queue = new int[maximumPoints];
        howManyIDominate = new int[maximumPoints];
        howManyDominateMe = new int[maximumPoints];
        whoIDominate = new int[maximumPoints][maximumPoints - 1];
    }

    @Override
    public String getName() {
        return "Fast Non-Dominated Sorting (original version)";
    }

    @Override
    protected void closeImpl() {
        queue = null;
        howManyIDominate = null;
        howManyDominateMe = null;
        whoIDominate = null;
    }

    private void pushToDominateList(int good, int bad) {
        ++howManyDominateMe[bad];
        whoIDominate[good][howManyIDominate[good]++] = bad;
    }

    private void comparePointWithPopulation(int index, List<T> population, int from) {
        T comparisonIndividual = population.get(index);
        double[] comparisonObjectives = comparisonIndividual.getObjectiveFunctionValues();

        int until = population.size();
        for (int j = from; j < until; ++j) {
            T loopIndividual = population.get(j);
            int comp = DominanceHelper.dominanceComparison(comparisonObjectives, loopIndividual.getObjectiveFunctionValues(), comparisonObjectives.length);
            switch (comp) {
                case -1:
                    pushToDominateList(index, j);
                    break;
                case +1:
                    pushToDominateList(j, index);
                    break;
            }
            double distance = euclideanDistance(comparisonIndividual, loopIndividual);
            comparisonIndividual.getSiblingsDistances().add(distance);
            loopIndividual.getSiblingsDistances().add(distance);
        }
    }

    public void compareExternalWithPopulation(T comparisonIndividual, List<T> population) {
        double[] comparisonObjectives = comparisonIndividual.getObjectiveFunctionValues();
        int howManyDominate = 0;
        int until = population.size();
        for (int j = 0; j < until; ++j) {
            T loopIndividual = population.get(j);
            int comp = DominanceHelper.dominanceComparison(comparisonObjectives, loopIndividual.getObjectiveFunctionValues(), comparisonObjectives.length);
            switch (comp) {
                case +1:
                    howManyDominate++;
                    break;
            }
            double distance = euclideanDistance(comparisonIndividual, loopIndividual);
            comparisonIndividual.getSiblingsDistances().add(distance);
        }
        comparisonIndividual.setHowManyDominateMe(howManyDominate);
    }

    private double euclideanDistance(T one, T other) {
        double sum = 0;
        for (int i = 0; i < one.getObjectiveFunctionValues().length; i++)
            sum += Math.pow(one.getObjectiveFunctionValues()[i] - other.getObjectiveFunctionValues()[i], 2);
        return Math.pow(sum, 0.5);
    }


    private void comparePoints(List<T> population) {
        int size = population.size();
        for (int i = 0; i < size; ++i) {
            comparePointWithPopulation(i, population, i + 1);
            population.get(i).setHowManyDominateMe(howManyDominateMe[i]);
        }
    }

    private int enqueueZeroRanks(int n, int[] ranks) {
        int qHead = 0;
        for (int i = 0; i < n; ++i) {
            if (howManyDominateMe[i] == 0) {
                ranks[i] = 0;
                queue[qHead++] = i;
            }
        }
        return qHead;
    }

    private int decreaseWhomIDominate(int index, int[] ranks, int qHead) {
        int[] iDominate = whoIDominate[index];
        int nextRank = ranks[index] + 1;
        for (int pos = howManyIDominate[index] - 1; pos >= 0; --pos) {
            int next = iDominate[pos];
            if (--howManyDominateMe[next] == 0) {
                ranks[next] = nextRank;
                queue[qHead++] = next;
            }
        }
        return qHead;
    }

    private void assignRanks() {
        int size = this.ranks.length;
        int qHead = enqueueZeroRanks(size, ranks);
        int qTail = 0;
        while (qHead > qTail) {
            int curr = queue[qTail++];
            qHead = decreaseWhomIDominate(curr, ranks, qHead);
        }
    }

    @Override
    protected void sortChecked(List<T> population, int maximalMeaningfulRank) {
        Arrays.fill(howManyDominateMe, 0);
        Arrays.fill(howManyIDominate, 0);
        comparePoints(population);
        Arrays.fill(ranks, -1);
        assignRanks();
    }
}
