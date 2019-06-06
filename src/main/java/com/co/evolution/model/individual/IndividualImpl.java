package com.co.evolution.model.individual;

import lombok.Getter;
import lombok.Setter;

public abstract class IndividualImpl<T> implements Individual {

    private T individual;

    @Setter
    @Getter
    private double fitness;

    @Setter
    @Getter
    private double[] objectiveValues;

    @Setter
    @Getter
    private int howManyDominateMe;

    @Setter
    @Getter
    private int paretoRank;

    @Setter
    @Getter
    private double penalization;

    @Getter
    @Setter
    private double[] diversityMeasures;

    public T get() {
        return individual;
    }

    public void set(T individual) {
        this.individual = individual;
    }

    public abstract Individual clone();

    @Override
    public boolean isFeasible() {
        return this.penalization == 0;
    }

    @Override
    public boolean isBetter(Individual other) {
        return other == null || this.fitness < other.getFitness();
    }

    @Override
    public abstract String toString();


}
