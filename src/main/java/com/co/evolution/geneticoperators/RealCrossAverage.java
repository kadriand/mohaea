package com.co.evolution.geneticoperators;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.GeneticOperator;

import java.util.ArrayList;
import java.util.List;

public class RealCrossAverage extends GeneticOperator<RealIndividual> {

    public RealCrossAverage() {
        this.cardinal = 2;
    }

    @Override
    public List<RealIndividual> apply(List<RealIndividual> individuals) {
        int dimensions = individuals.get(0).getDimensions();
        List<RealIndividual> newIndividuals = new ArrayList<>();
        RealIndividual ri = new RealIndividual(dimensions);
        for (int i = 0; i < dimensions; i++) {
            ri.get()[i] = (individuals.get(0).get()[i] + individuals.get(1).get()[i]) / 2;
        }
        newIndividuals.add(ri);
        return newIndividuals;
    }
}
