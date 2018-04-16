package com.co.evolution.geneticoperators;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class RealMutation extends GeneticOperator<RealIndividual> {

    private Double[] min;
    private Double[] max;

    public RealMutation(double sigma, double mean) {
        this.cardinal = 1;
    }

    public RealMutation(Double[] min, Double[] max) {
        this.cardinal = 1;
        this.min = min;
        this.max = max;
    }

    @Override
    public List<RealIndividual> apply(List<RealIndividual> individuals) {
        int dimensions = individuals.get(0).getDimensions();
        List<RealIndividual> newIndividuals = new ArrayList<>();
        for (RealIndividual individual : individuals) {
            RealIndividual ri = new RealIndividual(dimensions);
            for (int i = 0; i < dimensions; i++) {
                double sigma = Math.abs(max[i] - min[i]) / 100;
                ri.get()[i] = RandomUtils.nextGaussian(sigma, 0.0) + individual.get()[i];
                if (ri.get()[i] > max[i])
                    ri.get()[i] = max[i];
                if (ri.get()[i] < min[i])
                    ri.get()[i] = min[i];
            }

            newIndividuals.add(ri);
        }
        return newIndividuals;
    }
}
