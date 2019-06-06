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
        Population<T> population = initialization.init(fitnessCalculation);

        int populationSize = population.size();
        T best = population.getBest();
        T bestBefore = null;

        double[][] operatorsProbabilities = new double[populationSize][geneticOperators.size()];
        for (int i = 0; i < populationSize; i++)
            for (int j = 0; j < geneticOperators.size(); j++)
                operatorsProbabilities[i][j] = 1.0 / geneticOperators.size();

        while (terminationCondition.keepIteratingCondition(iteration, best, bestBefore)) {
            evolutionInterceptor.intercept(population, iteration, false);
            Population<T> newPopulation = new Population<>();
            selectionMethod.init(population);

            for (T individual : population) {
                int individualIdx = population.indexOf(individual);
                int genetOperatorRouletteIdx = RandomUtils.nextIntegerWithDefinedDistribution(operatorsProbabilities[individualIdx]);
                GeneticOperator<T> geneticOperator = geneticOperators.get(genetOperatorRouletteIdx);
                List<T> parents = new ArrayList<>();
                parents.add(individual);
                if (geneticOperator.getCardinal() > 1) {
                    List<T> selectedParents = selectionMethod.select(population, geneticOperator.getCardinal() - 1);
                    parents.addAll(selectedParents);
                }

                List<T> children = geneticOperator.apply(parents);
                for (T child : children) {
                    child.setObjectiveValues(fitnessCalculation.computeObjectives(child));
                    child.setFitness(fitnessCalculation.computeIndividualFitness(child, population));
                }

                Population<T> childrenTempPopulation = new Population<>(children);
                T childrenBest = childrenTempPopulation.getBest();
                double deltaSign = childrenBest.isBetter(individual) ? 1.0 : -1.0;//reward: punish
                modifyProbabilities(deltaSign, operatorsProbabilities[individualIdx], genetOperatorRouletteIdx);
                newPopulation.add(childrenBest);
            }

            population = newPopulation;
            fitnessCalculation.computePopulationFitness(population);
            bestBefore = best;
            best = population.getBest();
            iteration++;
        }
        evolutionInterceptor.intercept(population, 0, true);

        population.stream().forEach(individual -> {
            for (int i = 0; i < fitnessCalculation.getObjectiveFunctions().length; i++)
                if (!fitnessCalculation.getObjectiveFunctions()[i].isMinimize())
                    individual.getObjectiveValues()[i] = individual.getObjectiveValues()[i] * -1.0;
        });

        return population;
    }

    private void modifyProbabilities(double sign, double[] prob, int geneticOperatorIdx) {
        double delta = RandomUtils.nextDouble(0, 1);
        delta *= sign;
        prob[geneticOperatorIdx] += delta;
        prob[geneticOperatorIdx] = prob[geneticOperatorIdx] < 0 ? prob[geneticOperatorIdx] * -1 : prob[geneticOperatorIdx];
        double sum = 0.0;
        for (double aProb : prob)
            sum += aProb;
        for (int i = 0; i < prob.length; i++)
            prob[i] /= sum;
    }


}
