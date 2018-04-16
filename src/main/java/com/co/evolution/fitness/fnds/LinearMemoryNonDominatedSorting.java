package com.co.evolution.fitness.fnds;


import com.co.evolution.fitness.fnds.util.ArrayHelper;
import com.co.evolution.fitness.fnds.util.DoubleArraySorter;
import com.co.evolution.model.individual.Individual;

import java.util.List;

public class LinearMemoryNonDominatedSorting extends NonDominatedSorting<Individual> {
    private int[] indices;
    private int[] ranksInner;
    private double[][] pointsData;
    private DoubleArraySorter sorter;

    public LinearMemoryNonDominatedSorting(int maximumPoints, int maximumDimension) {
        super(maximumPoints, maximumDimension);
    }

    @Override
    protected void restore() {
        sorter = new DoubleArraySorter(maximumPoints);
        indices = new int[maximumPoints];
        ranksInner = new int[maximumPoints];
        pointsData = new double[maximumPoints][];
    }

    @Override
    public String getName() {
        return "Fast Non-Dominated Sorting (with linear memory)";
    }

    @Override
    protected void closeImpl() {
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

    @Override
    protected void sortChecked(List<Individual> population, int maximalMeaningfulRank) {
        int n = population.size();
        int dim = population.get(0).getObjectiveFunctionValues().length;
        ArrayHelper.fillIdentity(indices, n);
        sorter.lexicographicalSort(population, indices, 0, n, dim);
        int newN = DoubleArraySorter.retainUniquePoints(population, indices, this.pointsData, this.ranks);
        doSorting(newN, dim, maximalMeaningfulRank);
        for (int i = 0; i < n; ++i) {
            this.ranks[i] = this.ranksInner[this.ranks[i]];
            this.pointsData[i] = null;
        }
    }
}
