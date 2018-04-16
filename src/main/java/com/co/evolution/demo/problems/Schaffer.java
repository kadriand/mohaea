package com.co.evolution.demo.problems;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class Schaffer extends ObjectiveFunction<RealIndividual> {

    private double substraction;

    public Schaffer(boolean minimize, double substraction) {
        super(minimize);
        this.substraction = substraction;
    }

    @Override
    public double apply(RealIndividual individual) {
        return Math.pow(individual.get()[0] - substraction, 2);
    }
}
