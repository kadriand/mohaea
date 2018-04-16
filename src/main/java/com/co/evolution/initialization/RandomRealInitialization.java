package com.co.evolution.initialization;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.PopulationInitialization;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RandomRealInitialization implements PopulationInitialization<RealIndividual> {

    int size;
    Double[] min;
    Double[] max;
    int dimensions;

    @Override
    public List<RealIndividual> init(FitnessCalculation<RealIndividual> fitnessCalculation) {
        List<RealIndividual> pop = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            RealIndividual realIndividual = new RealIndividual(dimensions);
            realIndividual.initRandom(min, max);
            int functionsSize = fitnessCalculation.getObjectiveFunctions().length;
            realIndividual.setObjectiveFunctionValues(new double[functionsSize]);
            for (int j = 0; j < functionsSize; j++)
                realIndividual.getObjectiveFunctionValues()[j] = fitnessCalculation.getObjectiveFunctions()[j].apply(realIndividual);
            pop.add(realIndividual);
        }
        fitnessCalculation.newGenerationApply(pop);
        for (RealIndividual realIndividual : pop)
            realIndividual.setFitness(fitnessCalculation.calculate(realIndividual, pop));
        return pop;
    }

    public List<RealIndividual> dummyInit() {
        List<RealIndividual> pop = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            RealIndividual realIndividual = new RealIndividual(dimensions);
            realIndividual.initRandom(min, max);
            pop.add(realIndividual);
            realIndividual.setObjectiveFunctionValues(new double[2]);
            for (int j = 0; j < 2; j++)
                realIndividual.getObjectiveFunctionValues()[j] = realIndividual.get()[j];
        }
        return pop;
    }
}
