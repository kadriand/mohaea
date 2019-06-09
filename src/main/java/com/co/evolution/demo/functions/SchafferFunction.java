package com.co.evolution.demo.functions;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class SchafferFunction extends ObjectiveFunction<RealIndividual> {

    private double substraction;

    public SchafferFunction(boolean minimize, double substraction) {
        super(minimize);
        this.substraction = substraction;
    }

    @Override
    public double compute(RealIndividual individual) {
        return Math.pow(individual.get()[0] - substraction, 2);
    }
}
