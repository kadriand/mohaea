package com.co.evolution.demo.functions;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class RastriginFunction extends ObjectiveFunction<RealIndividual> {

    public RastriginFunction(boolean minimize) {
        super(minimize);
    }

    @Override
    public double compute(RealIndividual individual) {
        int dimensions = individual.getDimensions();
        double sum = 0.0;
        for (int i = 0; i < dimensions; i++) {
            double value = individual.get()[i];
            sum += ((value * value) - (10*Math.cos(2*Math.PI*value)));
        }
        return 10 * dimensions + sum;
    }
}
