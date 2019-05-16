package com.co.evolution.model.individual;

import java.util.List;

public interface Individual extends Comparable<Individual> {

    double getFitness();

    void setFitness(double fitness);

    double[] getObjectiveValues();

    void setObjectiveValues(double[] doubles);

    int getParetoRank();

    void setParetoRank(int paretoRank);

    double getPenalization();

    void setPenalization(double penalization);

    int getHowManyDominateMe();

    void setHowManyDominateMe(int howManyIDominate);

    List<Double> getDiversityMeasures();

    @Override
    int compareTo(Individual individual);

    @Override
    String toString();

}
