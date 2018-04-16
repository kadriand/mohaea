package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;
import lombok.Getter;

import java.util.List;

public abstract class GeneticOperator<T extends Individual> {

    /**
     * Number of parents the operator uses
     */
    @Getter
    protected int cardinal;

    public abstract List<T> apply(List<T> individuals);

}
