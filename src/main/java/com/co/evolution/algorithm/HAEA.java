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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class HAEA<T extends Individual> extends Algorithm<T> {

    public HAEA(List<GeneticOperator<T>> geneticOperators, TerminationCondition<T> terminationCondition, SelectionMethod<T> selectionMethod, PopulationInitialization<T> initialization, FitnessCalculation<T> fitnessCalculation) {
        super(geneticOperators, terminationCondition, selectionMethod, initialization, fitnessCalculation, new DefaultInterceptor<>());
    }

    @Override
    public Population<T> apply() {
        int iteration = 1;
        Population<T> parentsPopulation = initialization.init(fitnessCalculation);
        T best = parentsPopulation.getBest();
        T bestBefore = null;

        Map<T, double[]> operatorsRates = new HashMap<>();
        double[] defaultProbabilities = new double[geneticOperators.size()];
        for (int p = 0; p < geneticOperators.size(); p++)
            defaultProbabilities[p] = 1.0 / geneticOperators.size();

        parentsPopulation.forEach(individual -> operatorsRates.put(individual, defaultProbabilities.clone()));

        while (terminationCondition.keepIteratingCondition(iteration, best, bestBefore)) {
            evolutionInterceptor.intercept(parentsPopulation, iteration, operatorsRates, false);
            Population<T> newPopulation = new Population<>();
            selectionMethod.init(parentsPopulation);

            for (T parent : parentsPopulation) {
                double[] parentOperatorsRates = operatorsRates.get(parent);
                int genetOperatorRouletteIdx = RandomUtils.nextIntegerWithDefinedDistribution(parentOperatorsRates);
                GeneticOperator<T> geneticOperator = geneticOperators.get(genetOperatorRouletteIdx);
                List<T> parents = new ArrayList<>();
                parents.add(parent);
                if (geneticOperator.getCardinal() > 1) {
                    List<T> selectedParents = selectionMethod.select(parentsPopulation, geneticOperator.getCardinal() - 1);
                    parents.addAll(selectedParents);
                }

                List<T> children = geneticOperator.apply(parents);
                for (T child : children) {
                    child.setObjectiveValues(fitnessCalculation.computeObjectives(child));
                    child.setFitness(fitnessCalculation.computeIndividualFitness(child, parentsPopulation));
                }
                T bestChild = new Population<>(children).getBest();

                boolean reward = bestChild.isBetter(parent);  // reward: punish
                updateRates(reward, parentOperatorsRates, genetOperatorRouletteIdx);

                operatorsRates.put(bestChild, parentOperatorsRates.clone());
                newPopulation.add(bestChild);
            }

            Population<T> population = new Population<>(parentsPopulation);
            population.addAll(newPopulation);
            fitnessCalculation.computePopulationFitness(population);
            population.sort(Comparator.comparing(Individual::getFitness));

            int size = initialization.getSize();
            for (int i = 0; i < size; i++) {
                T individualToRemove = population.remove(size);
                operatorsRates.remove(individualToRemove);
            }
            parentsPopulation = population;

            bestBefore = best;
            best = parentsPopulation.getBest();
            iteration++;
        }
        evolutionInterceptor.intercept(parentsPopulation, 0, operatorsRates, false);

        for (T individual : parentsPopulation)
            for (int i = 0; i < fitnessCalculation.getObjectiveFunctions().length; i++)
                if (!fitnessCalculation.getObjectiveFunctions()[i].isMinimize())
                    individual.getObjectiveValues()[i] *= -1.0;

        return parentsPopulation;
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
