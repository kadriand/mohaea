package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

public class ParetoPlotterImageInterceptor<T extends Individual> extends EvolutionInterceptor<T> {

    private String imagesPathPrefix;
    private double[] functionsSigns;

    public ParetoPlotterImageInterceptor(int generationsGap, String prefix, ObjectiveFunction... objectiveFunctions) {
        this.functionsSigns = new double[objectiveFunctions.length];
        for (int i = 0; i < objectiveFunctions.length; i++)
            this.functionsSigns[i] = objectiveFunctions[i].isMinimize() ? 1.0 : -1.0;
        this.generationsGap = generationsGap;
        this.imagesPathPrefix = prefix;
    }

    @Override
    public void apply(Population<T> population, int generation) {
        System.out.println("Value: " + population.getBest().toString() + " Fitness: " + population.getBest().getFitness() + " Value: " + Arrays.toString(population.getBest().getObjectiveValues()));
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population, this.functionsSigns);
        File scatterPlotFile = paretoPlotter.toFile(imagesPathPrefix + "iteration-" + generation);

        try {
            System.out.println("Pareto fronts stored in " + scatterPlotFile.getCanonicalPath());

            File pointsFile = new File(String.format("output/%siteration-%s.csv", imagesPathPrefix, generation));
            pointsFile.getParentFile().mkdirs();
            StringBuilder solventInfo = new StringBuilder();
            solventInfo
                    .append("objective1")
                    .append(",objective2")
                    .append(",fitness")
                    .append(",rank")
                    .append(",penalization");
            population.forEach(individual -> solventInfo
                    .append("\n" + individual.getObjectiveValues()[0])
                    .append("," + individual.getObjectiveValues()[1])
                    .append("," + individual.getFitness())
                    .append("," + individual.getParetoRank())
                    .append("," + individual.getPenalization())
            );
            Files.write(pointsFile.toPath(), solventInfo.toString().getBytes());
            System.out.println("Pareto points stored in " + pointsFile.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
