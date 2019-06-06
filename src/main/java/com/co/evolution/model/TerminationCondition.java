package com.co.evolution.model;

public interface TerminationCondition<T> {

    boolean keepIteratingCondition(int actualIteration, T bestIndividual, T bestBefore);

}
