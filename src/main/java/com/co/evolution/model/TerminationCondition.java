package com.co.evolution.model;

public interface TerminationCondition<T> {

    boolean getCondition(int actualIteration, T bestIndividual, T bestBefore);

}
