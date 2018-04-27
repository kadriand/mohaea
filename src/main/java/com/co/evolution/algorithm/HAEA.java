package com.co.evolution.algorithm;

import com.co.evolution.interceptor.DefaultInterceptor;
import com.co.evolution.model.Algorithm;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.model.Population;
import com.co.evolution.model.PopulationInitialization;
import com.co.evolution.model.SelectionMethod;
import com.co.evolution.model.TerminationCondition;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class HAEA<T extends Individual> extends Algorithm<T> {

    public HAEA(List<GeneticOperator<T>> geneticOperators, TerminationCondition<T> terminationCondition, SelectionMethod<T> selectionMethod, PopulationInitialization<T> initialization, FitnessCalculation<T> fitnessCalculation) {
        super(geneticOperators, terminationCondition, selectionMethod, initialization, fitnessCalculation, new DefaultInterceptor<>());
    }

    @Override
    public Population<T> apply() {
        int iteration = 1;
        Population<T> pop = initialization.init(fitnessCalculation);

        int populationSize = pop.size();
        T best = pop.getBest();
        T bestBefore = null;

        double[][] operatorsProbabilities = new double[populationSize][geneticOperators.size()];
        for (int i = 0; i < populationSize; i++)
            for (int j = 0; j < geneticOperators.size(); j++)
                operatorsProbabilities[i][j] = 1.0 / geneticOperators.size();


        while (terminationCondition.getCondition(iteration, best, bestBefore)) {
            evolutionInterceptor.apply(iteration, pop);
            Population<T> newPop = new Population<>();
            selectionMethod.init(pop);

            for (int i = 0; i < populationSize; i++) {
                T actualIndividual = pop.get(i);
                int selectedOGIndex = RandomUtils.nextIntegerWithDefinedDistribution(operatorsProbabilities[i]);
                GeneticOperator<T> selectedGO = geneticOperators.get(selectedOGIndex);
                List<T> parents = new ArrayList<>();
                parents.add(actualIndividual);
                if (selectedGO.getCardinal() > 1) {
                    List<T> selectedParents = selectionMethod.select(pop, selectedGO.getCardinal() - 1);
                    parents.addAll(selectedParents);
                }

                List<T> children = selectedGO.apply(parents);
                int functionsSize = fitnessCalculation.getObjectiveFunctions().length;
                for (T child : children) {
                    child.setObjectiveValues(new double[functionsSize]);
                    for (int j = 0; j < functionsSize; j++)
                        child.getObjectiveValues()[j] = fitnessCalculation.getObjectiveFunctions()[j].compute(child);
                    child.setFitness(fitnessCalculation.calculate(child, pop));
                }

                Population<T> childrenPop = new Population<>();
                childrenPop.addAll(children);
                childrenPop.add(actualIndividual);

                T childrenBest = childrenPop.getBest();
                if (childrenBest == actualIndividual || childrenBest.getFitness() == actualIndividual.getFitness()) //punish
                    modifyProbabilities(-1, operatorsProbabilities[i], selectedOGIndex);
                else  //reward
                    modifyProbabilities(1, operatorsProbabilities[i], selectedOGIndex);
                newPop.add(childrenBest);
            }

            pop = newPop;
            fitnessCalculation.newGenerationApply(pop);
            for (T individual : pop)
                individual.setFitness(fitnessCalculation.calculate(individual, pop));

            bestBefore = best;
            best = pop.getBest();
            iteration++;
        }
        evolutionInterceptor.apply(pop);

        pop.stream().forEach(individual -> {
            for (int i = 0; i < fitnessCalculation.getObjectiveFunctions().length; i++)
                if (!fitnessCalculation.getObjectiveFunctions()[i].isMinimize())
                    individual.getObjectiveValues()[i] = individual.getObjectiveValues()[i] * -1.0;
        });

        return pop;
    }

    private void modifyProbabilities(int sign, double[] prob, int selectedOGIndex) {
        double num = RandomUtils.nextDouble(0, 1);
        num *= sign;
        prob[selectedOGIndex] += num;
        prob[selectedOGIndex] = prob[selectedOGIndex] < 0 ? prob[selectedOGIndex] * -1 : prob[selectedOGIndex];
        double sum = 0.0;
        for (double aProb : prob) {
            sum += aProb;
        }
        for (int i = 0; i < prob.length; i++) {
            prob[i] /= sum;
        }
    }


}
