package com.co.evolution.model.individual;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @Getter
    private List<Double> diversityMeasures = new ArrayList<>();

    public T get() {
        return individual;
    }

    public void set(T individual) {
        this.individual = individual;
    }

    public abstract void initRandom(T min, T max);

    public abstract Individual clone();

    @Override
    public int compareTo(Individual individual) {
        return Double.compare(this.fitness, individual.getFitness());
    }

    @Override
    public abstract String toString();


}
