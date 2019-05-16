package com.co.evolution.individual;

import com.co.evolution.model.individual.Individual;
import com.co.evolution.model.individual.IndividualImpl;
import lombok.Data;

import java.util.Arrays;

@Data
public class RealIndividual extends IndividualImpl<Double[]> {

    private int dimensions;

    public RealIndividual(int dimensions) {
        this.dimensions = dimensions;
        set(new Double[dimensions]);
    }

    @Override
    public Individual clone() {
        RealIndividual ri = new RealIndividual(this.dimensions);
        for (int i = 0; i < dimensions; i++) {
            ri.get()[i] = this.get()[i];
        }
        ri.setFitness(getFitness());
        return ri;
    }

    public String toString() {
        return Arrays.toString(get());
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }
}
