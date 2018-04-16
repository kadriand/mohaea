package com.co.evolution.objectivefunction.sch;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class SCH_f2 extends ObjectiveFunction<RealIndividual> {
    public SCH_f2(boolean minimize) {
        super(minimize);
    }

    @Override
    public double apply(RealIndividual individual) {
        return (individual.get()[0] - 2.0) * (individual.get()[0] - 2.0);
    }
}
