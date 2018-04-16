package com.co.evolution.fitness;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class RealFitnessCalculation extends Spea2FitnessCalculation<RealIndividual> {

    public RealFitnessCalculation(ObjectiveFunction<RealIndividual>[] objectiveFunctions) {
        super(objectiveFunctions);
    }

}
