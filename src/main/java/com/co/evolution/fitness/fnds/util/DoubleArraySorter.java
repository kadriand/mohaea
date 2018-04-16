package com.co.evolution.fitness.fnds.util;

import com.co.evolution.model.individual.Individual;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class DoubleArraySorter {
    private final double[] scratch;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private List<Individual> population = null;
    private int[] indices = null;
    private int coordinate = -1;
    private int maxCoordinate = -1;

    public DoubleArraySorter(int maximumPoints) {
        this.scratch = new double[maximumPoints];
    }

    private void sortImplInside(int from, int until) {
        double pivot = scratch[random.nextInt(from, until)];
        int l = from, r = until - 1;
        while (l <= r) {
            while (scratch[l] < pivot) ++l;
            while (scratch[r] > pivot) --r;
            if (l <= r) {
                ArrayHelper.swap(indices, l, r);
                ArrayHelper.swap(scratch, l++, r--);
            }
        }
        if (from + 1 <= r) sortImplInside(from, r + 1);
        if (l + 1 < until) sortImplInside(l, until);
    }

    private void sortImpl(int from, int until) {
        for (int i = from; i < until; ++i) {
            scratch[i] = population.get(indices[i]).getObjectiveFunctionValues()[coordinate];
        }
        sortImplInside(from, until);
    }

    private void lexSortImpl(int from, int until, int coordinate) {
        this.coordinate = coordinate;
        sortImpl(from, until);

        if (coordinate + 1 < maxCoordinate) {
            int last = from;
            double lastX = scratch[from];
            for (int i = from + 1; i < until; ++i) {
                double currX = scratch[i];
                if (currX != lastX) {
                    if (last + 1 < i) {
                        lexSortImpl(last, i, coordinate + 1);
                    }
                    last = i;
                    lastX = currX;
                }
            }
            if (last + 1 < until) {
                lexSortImpl(last, until, coordinate + 1);
            }
        }
    }

    public void lexicographicalSort(List<Individual> population, int[] indices, int from, int until, int maxCoordinate) {
        if (until - from > scratch.length) {
            throw new IllegalArgumentException("The maximum array length to be sorted is " + scratch.length
                    + ", but you requested from = " + from + " until = " + until + " which is " + (until - from));
        }
        this.population = population;
        this.indices = indices;
        this.maxCoordinate = maxCoordinate;

        lexSortImpl(from, until, 0);

        this.population = null;
        this.indices = null;
        this.maxCoordinate = -1;
    }

    public static int retainUniquePoints(List<Individual> sourcePopulation, int[] sortedIndices, double[][] targetPoints, int[] reindex) {
        int newN = 1;
        int lastII = sortedIndices[0];
        targetPoints[0] = sourcePopulation.get(lastII).getObjectiveFunctionValues();
        reindex[lastII] = 0;
        for (int i = 1; i < sourcePopulation.size(); ++i) {
            int currII = sortedIndices[i];
            if (!ArrayHelper.equal(sourcePopulation.get(lastII).getObjectiveFunctionValues(), sourcePopulation.get(currII).getObjectiveFunctionValues())) {
                // Copying the point to the internal array.
                targetPoints[newN] = sourcePopulation.get(currII).getObjectiveFunctionValues();
                lastII = currII;
                ++newN;
            }
            reindex[currII] = newN - 1;
        }
        return newN;
    }
}
