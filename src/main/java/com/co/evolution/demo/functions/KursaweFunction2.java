package com.co.evolution.demo.functions;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class KursaweFunction2 extends ObjectiveFunction<RealIndividual> {

    public KursaweFunction2(boolean minimize) {
        super(minimize);
    }

    @Override
    public double compute(RealIndividual individual) {
        double result = 0;
        for (int i = 0; i < 3; i++)
            result += Math.pow(Math.abs(individual.get()[i]), 0.8) + 5 * Math.sin(individual.get()[i] * individual.get()[i] * individual.get()[i]);
        return result;
    }
}
