package com.co.evolution.fitness.fnds;

import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.List;

/**
 * This is the base for classes which actually perform non-dominated sorting.
 *
 * @author Maxim Buzdalov
 */
public abstract class NonDominatedSorting<T extends Individual> implements AutoCloseable {
    @Getter
    protected boolean closeWasCalled = false;
    @Getter
    protected final int maximumPoints;
    @Getter
    protected final int maximumDimension;
    @Getter
    protected int[] ranks;

    protected NonDominatedSorting(int maximumPoints, int maximumObjectives) {
        this.maximumPoints = maximumPoints;
        this.maximumDimension = maximumObjectives;
    }

    protected abstract void restore();

    /**
     * Returns the name of the algorithm.
     *
     * @return the name of the algorithm.
     */
    public abstract String getName();


    /**
     * Releases all resources taken by the non-dominated sorting algorithm.
     */
    @Override
    public final void close() {
        if (closeWasCalled) {
            throw new IllegalStateException("close() has already been called");
        }
        closeWasCalled = true;
        closeImpl();
    }

    /**
     * Performs non-dominated sorting.
     *
     * @param population the list of individuals to be sorted.
     */
    public final void sort(List<T> population) {
        sort(population, population.size());
    }

    /**
     * Performs non-dominated sorting. All ranks above the given {@code maximalMeaningfulRank} will be reported
     * as {@code maximalMeaningfulRank + 1}.
     *
     * @param population            the array of points to be sorted.
     * @param maximalMeaningfulRank the maximal rank which is meaningful to the caller.
     *                              All ranks above will be reported as {@code maximalMeaningfulRank + 1}.
     *                              The safe value to get all ranks correct is {@code points.length}.
     */
    public final void sort(List<T> population, int maximalMeaningfulRank) {
        restore();
        this.ranks = new int[population.size()];

        if (population.size() == 0) {
            // Nothing to be done here.
            return;
        }

        sortChecked(population, maximalMeaningfulRank);
        filterMaximumMeaningfulRank(maximalMeaningfulRank);
    }

    /**
     * Performs actual release of any resources hold by the algorithm.
     */
    protected abstract void closeImpl();

    /**
     * Performs actual sorting. Assumes the input arrays are valid.
     *
     * @param population                the points to be sorted.
     * @param maximalMeaningfulRank the maximal rank which is meaningful to the caller.
     *                              All ranks above can be treated as same.
     */
    protected abstract void sortChecked(List<T> population, int maximalMeaningfulRank);

    private void filterMaximumMeaningfulRank(int maximalMeaningfulRank) {
        for (int i = 0; i < this.ranks.length; ++i) {
            if (this.ranks[i] > maximalMeaningfulRank) {
                this.ranks[i] = maximalMeaningfulRank + 1;
            }
        }
    }

}
