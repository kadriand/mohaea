/*
* Objective function = x^2
* */
package com.co.evolution.objectivefunction;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class DummyObjectiveFunction extends ObjectiveFunction<RealIndividual> {
    public DummyObjectiveFunction(boolean minimize) {
        super(minimize);
    }

    @Override
    public double apply(RealIndividual individual) {
        double sum = 0.0;
        for (int i = 0; i < individual.getDimensions(); i++) {
            sum += individual.get()[i] * individual.get()[i];
        }
        return sum;
    }
}
