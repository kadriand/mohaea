package com.co.evolution.model.individual;

import java.util.List;

public interface Individual extends Comparable<Individual> {

    double getFitness();

    void setFitness(double fitness);

    double[] getObjectiveFunctionValues();

    void setObjectiveFunctionValues(double[] doubles);

    int getHowManyDominateMe();

    void setHowManyDominateMe(int howManyIDominate);

    List<Double> getSiblingsDistances();

    @Override
    int compareTo(Individual individual);

    @Override
    String toString();

}
