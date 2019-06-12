package com.co.evolution.demo;

import com.co.evolution.algorithm.HAEA;
import com.co.evolution.demo.functions.SchafferFunction;
import com.co.evolution.fitness.CrowdingDistanceFitnessCalculation;
import com.co.evolution.geneticoperators.RealCrossAverage;
import com.co.evolution.geneticoperators.RealMutation;
import com.co.evolution.geneticoperators.RealPickRandom;
import com.co.evolution.individual.RealIndividual;
import com.co.evolution.initialization.RandomRealInitialization;
import com.co.evolution.interceptor.ParetoPlotterImageInterceptor;
import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Population;
import com.co.evolution.model.PopulationInitialization;
import com.co.evolution.model.SelectionMethod;
import com.co.evolution.selection.TournamentSelection;
import com.co.evolution.termination.MaxIterationsTerminationCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Schaffer {

    public static void main(String args[]) {
        schaffer();
    }

    public static void schaffer() {
        int dimensions = 1;

        Double[] min = new Double[dimensions];
        Double[] max = new Double[dimensions];

        int MAX_ITERATIONS = 200;
        int POPULATION_SIZE = 100;

        ObjectiveFunction[] objectiveFunctions = new ObjectiveFunction[2];

        Arrays.fill(min, -10.0);
        Arrays.fill(max, 10.0);
        objectiveFunctions[0] = new SchafferFunction(true, 0);
        objectiveFunctions[1] = new SchafferFunction(true, 2);

        List<GeneticOperator<RealIndividual>> geneticOperators = new ArrayList<>();
        geneticOperators.add(new RealCrossAverage());
        geneticOperators.add(new RealMutation(min, max));
        geneticOperators.add(new RealPickRandom(min, max, 1));

        MaxIterationsTerminationCondition terminationCondition = new MaxIterationsTerminationCondition(MAX_ITERATIONS);

        SelectionMethod<RealIndividual> selectionMethod = new TournamentSelection(4);

        PopulationInitialization<RealIndividual> initialization = new RandomRealInitialization(POPULATION_SIZE, min, max, dimensions);

        FitnessCalculation<RealIndividual> fitnessCalculation = new CrowdingDistanceFitnessCalculation<>(objectiveFunctions);
        EvolutionInterceptor<RealIndividual> evolutionInterceptor = new ParetoPlotterImageInterceptor<>(MAX_ITERATIONS / 5, "scha/nsga2-", objectiveFunctions);
        //        FitnessCalculation<RealIndividual> fitnessCalculation = new StrengthParetoFitnessCalculation<RealIndividual>(objectiveFunctions);
        //        EvolutionInterceptor<RealIndividual> evolutionInterceptor = new ParetoPlotterImageInterceptor<>(MAX_ITERATIONS / 5, "scha/spea2-", objectiveFunctions);

//        evolutionInterceptor = new OperatorsRatesInterceptor<>("scha/spea2-", geneticOperators, "tsv", "\t");
        //        GeneticAlgorithm<RealIndividual> ga = new GeneticAlgorithm<>(geneticOperators, terminationCondition, selectionMethod,true, initialization, fitnessCalculation);
        HAEA<RealIndividual> ga = new HAEA<>(geneticOperators, terminationCondition, selectionMethod, initialization, fitnessCalculation);
        ga.setEvolutionInterceptor(evolutionInterceptor);

        Population<RealIndividual> finalPop = ga.apply();

        RealIndividual best = finalPop.getBest();
        System.out.println("Value: " + best.toString() + " Fitness: " + best.getFitness() + " Value: " + Arrays.toString(best.getObjectiveValues()));
    }


}
