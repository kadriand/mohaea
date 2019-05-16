package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

public abstract class Penalization<T extends Individual> {

    protected abstract double compute(T individual);

    public double apply(T individual) {
        double penalization = compute(individual);
        individual.setPenalization(penalization);
        return penalization;
    }

}
