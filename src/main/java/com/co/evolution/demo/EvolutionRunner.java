package com.co.evolution.demo;

import com.co.evolution.algorithm.HAEA;
import com.co.evolution.demo.problems.KursaweFunction1;
import com.co.evolution.demo.problems.KursaweFunction2;
import com.co.evolution.fitness.ParetoPlotterInterceptor;
import com.co.evolution.fitness.RealFitnessCalculation;
import com.co.evolution.geneticoperators.RealCrossAverage;
import com.co.evolution.geneticoperators.RealMutation;
import com.co.evolution.geneticoperators.RealPickRandom;
import com.co.evolution.individual.RealIndividual;
import com.co.evolution.initialization.RandomRealInitialization;
import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.FitnessCalculation;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.PopulationInitialization;
import com.co.evolution.model.SelectionMethod;
import com.co.evolution.model.TerminationCondition;
import com.co.evolution.selection.TournamentSelection;
import com.co.evolution.terminationcondition.IterationsTerminationCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvolutionRunner {

    public static void main(String args[]) {
                int dimensions = 3;
        //                        int dimensions = 1;
//        int dimensions = 30;

        Double[] min = new Double[dimensions];
        Double[] max = new Double[dimensions];

        int MAX_ITERATIONS = 100;
        int POPULATION_SIZE = 100;

        ObjectiveFunction[] objectiveFunctions = new ObjectiveFunction[2];

        objectiveFunctions[0] = new KursaweFunction1(true);
        objectiveFunctions[1] = new KursaweFunction2(true);
        Arrays.fill(min, -5.0);
        Arrays.fill(max, 5.0);

        //                Arrays.fill(min, -10.0);
        //                Arrays.fill(max, 10.0);
        //                objectiveFunctions[0] = new Schaffer(true, 0);
        //                objectiveFunctions[1] = new Schaffer(true, 2);

//        Arrays.fill(min, 0.0);
//        Arrays.fill(max, 1.0);
//        objectiveFunctions[0] = new ZitzlerDebThiele.ZDT2_F1(true);
//        objectiveFunctions[1] = new ZitzlerDebThiele.ZDT2_F2(true);

        List<GeneticOperator<RealIndividual>> geneticOperators = new ArrayList<>();
        geneticOperators.add(new RealCrossAverage());
        geneticOperators.add(new RealMutation(min, max));
        geneticOperators.add(new RealPickRandom(min, max, 1));

        TerminationCondition<RealIndividual> terminationCondition = new IterationsTerminationCondition(MAX_ITERATIONS);

        SelectionMethod<RealIndividual> selectionMethod = new TournamentSelection(4);

        PopulationInitialization<RealIndividual> initialization = new RandomRealInitialization(POPULATION_SIZE, min, max, dimensions);

        FitnessCalculation<RealIndividual> fitnessCalculation = new RealFitnessCalculation(objectiveFunctions);

        EvolutionInterceptor<RealIndividual> evolutionInterceptor = new ParetoPlotterInterceptor<>(MAX_ITERATIONS / 5);

        //        GeneticAlgorithm<RealIndividual> ga = new GeneticAlgorithm<>(geneticOperators, terminationCondition, selectionMethod,true, initialization, fitnessCalculation);
        HAEA<RealIndividual> ga = new HAEA<>(geneticOperators, terminationCondition, selectionMethod, true, initialization, fitnessCalculation, evolutionInterceptor);

        List<RealIndividual> finalPop = ga.apply();

        RealIndividual best = ga.getBest(finalPop);
        System.out.println("Value: " + best.toString() + " Fitness: " + best.getFitness() + " Value: " + Arrays.toString(best.getObjectiveFunctionValues()));
    }

}
