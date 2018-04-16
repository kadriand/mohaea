package com.co.evolution.individual;

import com.co.evolution.model.individual.Individual;
import com.co.evolution.model.individual.IndividualImpl;
import com.co.evolution.util.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class RealIndividual extends IndividualImpl<Double[]> {

    @Getter
    @Setter
    private int dimensions;

    public RealIndividual(int dimensions)
    {
        this.dimensions = dimensions;
        set(new Double[dimensions]);
    }

    @Override
    public void initRandom(Double[] min, Double max[]) {
        for (int i = 0; i < dimensions; i++) {
            get()[i] = RandomUtils.nextDouble(min[i], max[i]);
        }
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

    @Override
    public String toString()
    {
        return Arrays.toString(get());
    }
}
