package com.co.evolution.fitness.fnds;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.initialization.RandomRealInitialization;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TortureTesting {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private static double[][] generateCloud(int points, int dimension) {
        double[][] rv = new double[points][dimension];
        if (random.nextBoolean()) {
            for (int i = 0; i < points; ++i) {
                for (int j = 0; j < dimension; ++j) {
                    rv[i][j] = random.nextDouble();
                }
            }
        } else {
            for (int i = 0; i < points; ++i) {
                for (int j = 0; j < dimension; ++j) {
                    rv[i][j] = random.nextInt(20);
                }
            }
        }
        return rv;
    }

    /**
     * https://github.com/mbuzdalov/non-dominated-sorting
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        int maxPoints = 100;
        int maxDimension = 2;
        List<NonDominatedSorting> sortings = Arrays.asList(
                new FastNonDominatedSorting<RealIndividual>(maxPoints, maxDimension),
                new LinearMemoryNonDominatedSorting(maxPoints, maxDimension)
        );

        int pointsNumber = 1 + random.nextInt(maxPoints);
        int dimension = 2;
        int maxRank = random.nextInt(pointsNumber + 1);
        System.out.println("Uniform hypercube with " + pointsNumber
                + " points, dimension " + dimension
                + ", max rank " + maxRank);
        System.out.println();
        double[][] pointsData = generateCloud(pointsNumber, dimension);

        int dimensions = 2;
        Double[] min = new Double[dimensions];
        Double[] max = new Double[dimensions];
        Arrays.fill(min, -5.0);
        Arrays.fill(max, 5.0);
        RandomRealInitialization initialization = new RandomRealInitialization(maxPoints, min, max, dimensions);
        List<RealIndividual> realIndividuals = initialization.dummyInit();

        int[] ranks;
        for (NonDominatedSorting sorting : sortings) {
            long t0 = System.currentTimeMillis();
            sorting.sort(realIndividuals);
            ranks = sorting.getRanks();
            long time = System.currentTimeMillis() - t0;
            System.out.printf("%102s: %d ms%n", sorting.getName(), time);
            System.out.println(Arrays.toString(ranks));
            System.out.println(Arrays.deepToString(pointsData));
            //            printTest(points, dimension, instance, ranks);

            pointsData = new double[maxPoints][2];
            for (int i = 0; i < realIndividuals.size(); i++) {
                pointsData[i][0]=realIndividuals.get(i).getObjectiveFunctionValues()[0];
                pointsData[i][1]=realIndividuals.get(i).getObjectiveFunctionValues()[1];
            }

            ParetoPlotterDouble paretoPlotterDouble = new ParetoPlotterDouble("pareto fronts", pointsData, ranks);
            paretoPlotterDouble.pack();
            paretoPlotterDouble.setVisible(true);
            paretoPlotterDouble.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        }

        for (NonDominatedSorting sorting : sortings) {
            long t0 = System.currentTimeMillis();
            sorting.sort(realIndividuals);
            ranks = sorting.getRanks();
            long time = System.currentTimeMillis() - t0;
            System.out.printf("%102s: %d ms%n", sorting.getName(), time);
            System.out.println(Arrays.toString(ranks));
            //            printTest(points, dimension, instance, ranks);
        }
    }
}
