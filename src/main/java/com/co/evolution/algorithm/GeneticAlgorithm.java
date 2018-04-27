/*
Implementation of mono-objective genetic algorithm.
Parameters:
Objective Function is the first of the objectiveFunctions list
Crossing operator is the firs of the geneticOperators list
Mutation operator is the second of the geneticOperator list


 */
package com.co.evolution.algorithm;

import com.co.evolution.model.Algorithm;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.model.Population;
import com.co.evolution.model.PopulationInitialization;
import com.co.evolution.model.SelectionMethod;
import com.co.evolution.model.TerminationCondition;
import com.co.evolution.model.individual.Individual;

import java.util.List;

public class GeneticAlgorithm<T extends Individual> extends Algorithm<T> {

    public GeneticAlgorithm(List<GeneticOperator<T>> geneticOperators, TerminationCondition<T> terminationCondition, SelectionMethod<T> selectionMethod, PopulationInitialization<T> initialization, FitnessCalculation<T> fitnessCalculation) {
        super(geneticOperators, terminationCondition, selectionMethod, initialization, fitnessCalculation,null);
    }

    @Override
    public Population<T> apply() {
        int iteration = 1;
        Population<T> pop = getInitialization().init(getFitnessCalculation());
        int size = pop.size();
        T best = pop.getBest();
        T bestBefore = null;
        GeneticOperator<T> crossOp = getGeneticOperators().get(0);
        GeneticOperator<T> mutateOp = getGeneticOperators().get(1);
        while(getTerminationCondition().getCondition(iteration, best, bestBefore))
        {
            System.out.println("Value: " + best.toString() + " Fitness: " + best.getFitness());
            Population<T> newPop = new Population<>();
            int numInd = 0;
            getSelectionMethod().init(pop);
            while(numInd < size)
            {
                List<T> parents = getSelectionMethod().select(pop, crossOp.getCardinal());
                List<T> crossingInd = crossOp.apply(parents);
                List<T> mutateInd = mutateOp.apply(crossingInd);
                for (T child : mutateInd) {
                    child.setFitness(getFitnessCalculation().calculate(child, pop));
                }
                newPop.addAll(mutateInd);
                numInd += mutateInd.size();
            }
            bestBefore = best;
            best = newPop.getBest();
            pop = newPop;
            iteration++;
        }
        return pop;
    }
}
