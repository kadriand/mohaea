package com.co.evolution.terminationcondition;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.TerminationCondition;

public class IterationsTerminationCondition implements TerminationCondition<RealIndividual> {

    private int maxIteration;

    public IterationsTerminationCondition(int maxIteration)
    {
        this.maxIteration = maxIteration;
    }

    @Override
    public boolean getCondition(int actualIteration, RealIndividual bestIndividual, RealIndividual bestBefore) {
        return actualIteration < maxIteration;
    }
}
