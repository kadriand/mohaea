package com.co.evolution.initialization;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.Population;
import com.co.evolution.model.PopulationInitialization;
import com.co.evolution.util.RandomUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RandomRealInitialization implements PopulationInitialization<RealIndividual> {

    private int size;
    private Double[] min, max;
    private int dimensions;

    @Override
    public Population<RealIndividual> init(FitnessCalculation<RealIndividual> fitnessCalculation) {
        Population<RealIndividual> pop = new Population<>();
        for (int i = 0; i < size; i++) {
            RealIndividual realIndividual = initRandom();
            int functionsSize = fitnessCalculation.getObjectiveFunctions().length;
            realIndividual.setObjectiveValues(new double[functionsSize]);
            for (int j = 0; j < functionsSize; j++)
                realIndividual.getObjectiveValues()[j] = fitnessCalculation.getObjectiveFunctions()[j].compute(realIndividual);
            pop.add(realIndividual);
        }
        fitnessCalculation.newGenerationApply(pop);
        for (RealIndividual realIndividual : pop)
            realIndividual.setFitness(fitnessCalculation.calculate(realIndividual, pop));
        return pop;
    }


    @Override
    public RealIndividual initRandom() {
        RealIndividual realIndividual = new RealIndividual(dimensions);
        for (int i = 0; i < dimensions; i++)
            realIndividual.get()[i] = RandomUtils.nextDouble(min[i], max[i]);
        return realIndividual;
    }

}
