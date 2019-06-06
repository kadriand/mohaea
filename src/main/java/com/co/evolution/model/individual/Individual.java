package com.co.evolution.model.individual;

public interface Individual {

    double getFitness();

    void setFitness(double fitness);

    double[] getObjectiveValues();

    void setObjectiveValues(double[] doubles);

    int getParetoRank();

    void setParetoRank(int paretoRank);

    double getPenalization();

    void setPenalization(double penalization);

    boolean isFeasible();

    int getHowManyDominateMe();

    void setHowManyDominateMe(int howManyIDominate);

    double[] getDiversityMeasures();

    void setDiversityMeasures(double[] measures);

    boolean isBetter(Individual individual);

    @Override
    String toString();

}
