package com.co.evolution.fitness.fnds;

import com.co.evolution.model.individual.Individual;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ArraySorter<T extends Individual> {
    private final double[] scratch;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private List<T> population = null;
    private int[] indices = null;
    private int coordinate = -1;
    private int maxCoordinate = -1;

    public ArraySorter(int maximumPoints) {
        this.scratch = new double[maximumPoints];
    }

    private void sortImplInside(int from, int until) {
        double pivot = scratch[random.nextInt(from, until)];
        int l = from, r = until - 1;
        while (l <= r) {
            while (scratch[l] < pivot) ++l;
            while (scratch[r] > pivot) --r;
            if (l <= r) {
                swap(indices, l, r);
                swap(scratch, l++, r--);
            }
        }
        if (from + 1 <= r) sortImplInside(from, r + 1);
        if (l + 1 < until) sortImplInside(l, until);
    }

    private void sortImpl(int from, int until) {
        for (int i = from; i < until; ++i)
            scratch[i] = population.get(indices[i]).getObjectiveValues()[coordinate];
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

    public void lexicographicalSort(List<T> population, int[] indices, int from, int until, int maxCoordinate) {
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

    public int retainUniquePoints(List<T> sourcePopulation, int[] sortedIndices, double[][] targetPoints, int[] reindex) {
        int newN = 1;
        int lastII = sortedIndices[0];
        targetPoints[0] = sourcePopulation.get(lastII).getObjectiveValues();
        reindex[lastII] = 0;
        for (int i = 1; i < sourcePopulation.size(); ++i) {
            int currII = sortedIndices[i];
            if (!equal(sourcePopulation.get(lastII).getObjectiveValues(), sourcePopulation.get(currII).getObjectiveValues())) {
                // Copying the point to the internal array.
                targetPoints[newN] = sourcePopulation.get(currII).getObjectiveValues();
                lastII = currII;
                ++newN;
            }
            reindex[currII] = newN - 1;
        }
        return newN;
    }

    public static void swap(int[] array, int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

    public static void swap(double[] array, int a, int b) {
        double tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

    public static boolean equal(double[] a, double[] b) {
        int al = a.length;
        return al == b.length && equal(a, b, al);
    }

    private static boolean equal(double[] a, double[] b, int prefixLength) {
        for (int i = 0; i < prefixLength; ++i) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void fillIdentity(int[] array, int n) {
        for (int i = 0; i < n; ++i) {
            array[i] = i;
        }
    }

    public static double max(double[] array, int from, int until) {
        if (from >= until) {
            return Double.NEGATIVE_INFINITY;
        } else {
            double rv = array[from];
            for (int i = from + 1; i < until; ++i) {
                double v = array[i];
                if (rv < v) {
                    rv = v;
                }
            }
            return rv;
        }
    }
}
