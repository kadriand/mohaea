package com.co.evolution.demo;

import com.co.evolution.geneticoperators.RealCrossAverage;
import com.co.evolution.geneticoperators.RealMutation;
import com.co.evolution.geneticoperators.RealPickRandom;
import com.co.evolution.individual.RealIndividual;
import com.co.evolution.initialization.RandomRealInitialization;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.PopulationInitialization;
import com.co.evolution.model.SelectionMethod;
import com.co.evolution.demo.problems.RastriginFunction;
import com.co.evolution.selection.TournamentSelection;
import com.co.evolution.termination.MaxIterationsTerminationCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmDemo {

    public static void main(String args[]) {
        int dimensions = 10;
        Double[] min = new Double[dimensions];
        Double[] max = new Double[dimensions];

        Arrays.fill(min, -5.12);
        Arrays.fill(max, 5.12);

        int MAX_ITERATIONS = 100;
        int POPULATION_SIZE = 50;

        ObjectiveFunction<RealIndividual>[] objectiveFunctions = new ObjectiveFunction[2];
        objectiveFunctions[0] = new RastriginFunction(true);

        List<GeneticOperator<RealIndividual>> geneticOperators = new ArrayList<>();
        geneticOperators.add(new RealCrossAverage());
        geneticOperators.add(new RealMutation(1.0, 0.0));
        geneticOperators.add(new RealPickRandom(4));

        com.co.evolution.model.TerminationCondition terminationCondition = new MaxIterationsTerminationCondition(MAX_ITERATIONS);

        SelectionMethod<RealIndividual> selectionMethod = new TournamentSelection(4);

        PopulationInitialization<RealIndividual> initialization = new RandomRealInitialization(POPULATION_SIZE, min, max, dimensions);

//        FitnessCalculation<RealIndividual> fitnessCalculation = new RealFitnessCalculation(objectiveFunctions);

        //GeneticAlgorithm<RealIndividual> ga = new GeneticAlgorithm<>(geneticOperators, terminationCondition, selectionMethod,true, initialization, fitnessCalculation);
//        HAEA<RealIndividual> ga = new HAEA<>(geneticOperators, terminationCondition, selectionMethod, true, initialization, fitnessCalculation);

//        List<RealIndividual> finalPop = ga.apply();
//
//        RealIndividual best = ga.findBest(finalPop);

//        System.out.println("Value: " + best.toString() + " Fitness: " + best.getFitness());
    }

}
