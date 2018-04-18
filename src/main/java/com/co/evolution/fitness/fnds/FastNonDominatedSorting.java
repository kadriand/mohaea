package com.co.evolution.fitness.fnds;

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

public class FastNonDominatedSorting<T extends Individual> {

    @Getter
    int maximumPoints;
    @Getter
    protected int[] ranks;

    List<T> population;

    int objectivesSize;
    int ranksSize;

    int[] queue;
    @Getter
    int[] howManyIDominate;
    @Getter
    int[] howManyDominateMe;
    @Getter
    int[][] whoIDominate;

    private static final int[] REINDEX = {0, -1, 1, 0};
    private static final int HAS_LESS_MASK = 1;
    private static final int HAS_GREATER_MASK = 2;

    public FastNonDominatedSorting(List<T> population, int objectivesSize) {
        this.population = population;
        this.maximumPoints = population.size();
        this.objectivesSize = objectivesSize;
        restore();
    }

    protected void restore() {
        queue = new int[maximumPoints];
        howManyIDominate = new int[maximumPoints];
        howManyDominateMe = new int[maximumPoints];
        whoIDominate = new int[maximumPoints][maximumPoints - 1];
    }

    public String getName() {
        return "Fast Non-Dominated Sorting (original version)";
    }

    /**
     * Good dominates bad
     *
     * @param good
     * @param bad
     */
    void pushToDominateList(int good, int bad) {
        ++howManyDominateMe[bad];
        whoIDominate[good][howManyIDominate[good]++] = bad;
    }

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
        }
    }

    int dominanceComparison(double[] a, double[] b) {
        int rv = detailedDominanceComparison(a, b, HAS_GREATER_MASK | HAS_LESS_MASK);
        return REINDEX[rv];
    }

    int detailedDominanceComparison(double[] a, double[] b, int breakMask) {
        int result = 0;
        for (int i = 0; i < objectivesSize; ++i) {
            double ai = a[i], bi = b[i];
            if (ai < bi) {
                result |= HAS_LESS_MASK;
            } else if (ai > bi) {
                result |= HAS_GREATER_MASK;
            }
            if ((result & breakMask) == breakMask) {
                break;
            }
        }
        return result;
    }

    void comparePoints() {
        int size = population.size();
        for (int i = 0; i < size; ++i)
            comparePointWithPopulation(i, i + 1);
    }

    int enqueueZeroRanks() {
        int qHead = 0;
        int size = ranks.length;
        for (int i = 0; i < size; ++i) {
            if (howManyDominateMe[i] == 0) {
                ranks[i] = 0;
                population.get(i).setParetoRank(0);
                queue[qHead++] = i;
            }
        }
        return qHead;
    }

    int decreaseWhomIDominate(int index, int qHead) {
        int[] iDominate = whoIDominate[index];
        int nextRank = ranks[index] + 1;
        for (int pos = howManyIDominate[index] - 1; pos >= 0; --pos) {
            int next = iDominate[pos];
            if (--howManyDominateMe[next] == 0) {
                ranks[next] = nextRank;
                ranksSize = nextRank > ranksSize ? nextRank : ranksSize;
                population.get(next).setParetoRank(nextRank);
                queue[qHead++] = next;
            }
        }
        return qHead;
    }

    void assignRanks() {
        int qHead = enqueueZeroRanks();
        int qTail = 0;
        while (qHead > qTail) {
            int curr = queue[qTail++];
            qHead = decreaseWhomIDominate(curr, qHead);
        }
    }

    public void sort() {
        restore();
        this.ranks = new int[population.size()];
        if (population.size() == 0)
            return;

        Arrays.fill(howManyDominateMe, 0);
        Arrays.fill(howManyIDominate, 0);
        comparePoints();
        Arrays.fill(ranks, -1);
        assignRanks();
    }
}
