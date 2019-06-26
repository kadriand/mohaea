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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class MOHAEA<T extends Individual> extends Algorithm<T> {

    public MOHAEA(List<GeneticOperator<T>> geneticOperators, TerminationCondition<T> terminationCondition, SelectionMethod<T> selectionMethod, PopulationInitialization<T> initialization, FitnessCalculation<T> fitnessCalculation) {
        super(geneticOperators, terminationCondition, selectionMethod, initialization, fitnessCalculation, new DefaultInterceptor<>());
    }

    @Override
    public Population<T> apply() {
        int iteration = 1;
        Population<T> population = initialization.init(fitnessCalculation);
        T best = population.getBest();
        T bestBefore = null;

        Map<T, double[]> operatorsRates = new HashMap<>();
        double[] defaultProbabilities = new double[geneticOperators.size()];
        for (int p = 0; p < geneticOperators.size(); p++)
            defaultProbabilities[p] = 1.0 / geneticOperators.size();
        for (T individual : population)
            operatorsRates.put(individual, defaultProbabilities.clone());

        while (terminationCondition.keepIteratingCondition(iteration, best, bestBefore)) {
            evolutionInterceptor.intercept(population, iteration, operatorsRates, false);

            List<T> parentsAndChildren = new ArrayList<>();
            Map<T, Population<T>> families = new HashMap<>();
            Map<T, Integer> individualOperatorIdxs = new HashMap<>();
            selectionMethod.init(population);

            for (T individual : population) {
                double[] parentOperatorsRates = operatorsRates.get(individual);
                int genetOperatorRouletteIdx = RandomUtils.nextIntegerWithDefinedDistribution(parentOperatorsRates);
                GeneticOperator<T> geneticOperator = geneticOperators.get(genetOperatorRouletteIdx);
                List<T> offspringParents = new ArrayList<>();
                offspringParents.add(individual);
                if (geneticOperator.getCardinal() > 1) {
                    List<T> selectedParents = selectionMethod.select(population, geneticOperator.getCardinal() - 1);
                    offspringParents.addAll(selectedParents);
                }
                List<T> children = geneticOperator.apply(offspringParents);
                children.forEach(child -> child.setObjectiveValues(fitnessCalculation.computeObjectives(child)));
                parentsAndChildren.addAll(children);
                families.put(individual, new Population<>(children));
                individualOperatorIdxs.put(individual, genetOperatorRouletteIdx);
            }

            parentsAndChildren.addAll(population);
            fitnessCalculation.computePopulationRanksFitness(parentsAndChildren);
            Population<T> newPopulation = new Population<>();
            Map<T, double[]> newOperatorsRates = new HashMap<>();
            for (Map.Entry<T, Population<T>> offspring : families.entrySet()) {
                T bestChild = offspring.getValue().getBest();
                T parent = offspring.getKey();
                double[] parentOperatorsRates = operatorsRates.get(parent);
                boolean reward = bestChild.isBetter(parent);
                updateRates(reward, parentOperatorsRates, individualOperatorIdxs.get(parent));
                T selectedIndividual = reward ? bestChild : parent;
                newOperatorsRates.put(selectedIndividual, parentOperatorsRates);
                newPopulation.add(selectedIndividual);
            }

            bestBefore = best;
            best = population.getBest();
            operatorsRates = newOperatorsRates;
            population = newPopulation;
            iteration++;
        }
        evolutionInterceptor.intercept(population, 0, operatorsRates, true);

        for (T individual : population)
            for (int i = 0; i < fitnessCalculation.getObjectiveFunctions().length; i++)
                if (!fitnessCalculation.getObjectiveFunctions()[i].isMinimize())
                    individual.getObjectiveValues()[i] *= -1.0;

        return population;
    }

    private void updateRates(boolean reward, double[] rates, int geneticOperatorIdx) {
        double delta = RandomUtils.nextDouble(0, 1);
        double sign = reward ? 1.0 : -1.0;
        rates[geneticOperatorIdx] *= (1.0 + delta * sign);
        double sum = DoubleStream.of(rates).sum();
        for (int i = 0; i < rates.length; i++)
            rates[i] /= sum;
    }


}
