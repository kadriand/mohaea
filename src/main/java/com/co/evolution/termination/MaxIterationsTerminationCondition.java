package com.co.evolution.termination;

import com.co.evolution.model.TerminationCondition;
import com.co.evolution.model.individual.Individual;

public class MaxIterationsTerminationCondition<T extends Individual> implements TerminationCondition<T> {

    private int maxIteration;

    public MaxIterationsTerminationCondition(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    @Override
    public boolean getCondition(int actualIteration, T bestIndividual, T bestBefore) {
        return actualIteration <= maxIteration;
    }
}
