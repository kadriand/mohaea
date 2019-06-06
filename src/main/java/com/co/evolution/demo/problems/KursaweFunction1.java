package com.co.evolution.demo.problems;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class KursaweFunction1 extends ObjectiveFunction<RealIndividual> {

    public KursaweFunction1(boolean minimize) {
        super(minimize);
    }

    @Override
    public double compute(RealIndividual individual) {
        double result = 0;
        for (int i = 0; i < 2; i++)
            result += -10.0 * Math.exp(-0.2 * Math.pow(individual.get()[i] * individual.get()[i] + individual.get()[i + 1] * individual.get()[i + 1], 0.5));
        return result;
    }
}
