package com.co.evolution.fitness.fnds;


import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.List;

public class LinearMemoryNonDominatedSorting<T extends Individual> {

    @Getter
    protected final int maximumPoints;
    @Getter
    protected final int maximumDimension;
    @Getter
    protected int[] ranks;

    private int[] indices;
    private int[] ranksInner;
    private double[][] pointsData;
    private ArraySorter sorter;

    public LinearMemoryNonDominatedSorting(int maximumPoints, int maximumDimension) {
        this.maximumPoints = maximumPoints;
        this.maximumDimension = maximumDimension;
    }

    protected void restore() {
        sorter = new ArraySorter(maximumPoints);
        indices = new int[maximumPoints];
        ranksInner = new int[maximumPoints];
        pointsData = new double[maximumPoints][];
    }

    public String getName() {
        return "Fast Non-Dominated Sorting (with linear memory)";
    }

    protected void close() {
        indices = null;
        ranksInner = null;
        pointsData = null;
        sorter = null;
    }

    private boolean strictlyDominatesAssumingNotSame(int goodIndex, int weakIndex, int dim) {
        double[] goodPoint = pointsData[goodIndex];
        double[] weakPoint = pointsData[weakIndex];
        // Comparison in 0 makes no sense, as due to goodIndex < weakIndex the pointsData are <= in this coordinate.
        for (int i = dim - 1; i > 0; --i) {
            if (goodPoint[i] > weakPoint[i]) {
                return false;
            }
        }
        return true;
    }

    private void doSorting(int n, int dim, int maximalMeaningfulRank) {
        ranksInner[0] = 0;
        for (int i = 1; i < n; ++i) {
            int myRank = 0;
            for (int j = i - 1; myRank <= maximalMeaningfulRank && j >= 0; --j) {
                int thatRank = ranksInner[j];
                if (myRank <= thatRank && strictlyDominatesAssumingNotSame(j, i, dim)) {
                    myRank = thatRank + 1;
                }
            }
            ranksInner[i] = myRank;
        }
    }

    /**
     * Performs non-dominated sorting.
     *
     * @param population the list of individuals to be sorted.
     */
    /**
     * Performs non-dominated sorting. All ranks above the given {@code maximalMeaningfulRank} will be reported
     * as {@code maximalMeaningfulRank + 1}.
     *
     * @param population the array of points to be sorted.
     */
    public void sort(List<T> population) {
        restore();
        this.ranks = new int[population.size()];
        if (population.size() == 0)
            return;

        int n = population.size();
        int dim = population.get(0).getObjectiveValues().length;
        ArraySorter.fillIdentity(indices, n);
        sorter.lexicographicalSort(population, indices, 0, n, dim);
        int newN = sorter.retainUniquePoints(population, indices, this.pointsData, this.ranks);
        doSorting(newN, dim, population.size());
        for (int i = 0; i < n; ++i) {
            this.ranks[i] = this.ranksInner[this.ranks[i]];
            this.pointsData[i] = null;
        }
    }
}
