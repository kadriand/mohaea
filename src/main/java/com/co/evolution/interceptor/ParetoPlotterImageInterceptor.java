package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParetoPlotterImageInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    private int generationsGap;
    private String filePrefix;
    private double[] functionsSigns;

    public ParetoPlotterImageInterceptor(int generationsGap, String prefix, ObjectiveFunction[] objectiveFunctions) {
        this.functionsSigns = new double[objectiveFunctions.length];
        for (int i = 0; i < objectiveFunctions.length; i++)
            this.functionsSigns[i] = objectiveFunctions[i].isMinimize() ? 1.0 : -1.0;
        this.generationsGap = generationsGap;
        this.filePrefix = prefix;
    }

    @Override
    public void apply(int generation, Population<T> population) {
        System.out.println("Value: " + population.getBest().toString() + " Fitness: " + population.getBest().getFitness() + " Value: " + Arrays.toString(population.getBest().getObjectiveValues()));
        if (generation % generationsGap != 0 && generation != 1)
            return;
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population, this.functionsSigns);
        File scatterPlotFile = paretoPlotter.toFile(filePrefix + "iteration-" + generation);
        try {
            System.out.println("Pareto fronts stored in " + scatterPlotFile.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(Population<T> population) {
        System.out.println("Value: " + population.getBest().toString() + " Fitness: " + population.getBest().getFitness() + " Value: " + Arrays.toString(population.getBest().getObjectiveValues()));
        List<T> populationList = population.stream().filter(t -> t.getParetoRank() == 0).collect(Collectors.toList());
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Last iteration ", populationList, this.functionsSigns);
        File scatterPlotFile = paretoPlotter.toFile(filePrefix + "iteration-last");
        try {
            System.out.println("Pareto fronts stored in " + scatterPlotFile.getCanonicalPath());

            File pointsFile = new File("output/" + filePrefix + "iteration-last.csv");
            pointsFile.getParentFile().mkdirs();
            StringBuilder solventInfo = new StringBuilder();
            solventInfo
                    .append("function1")
                    .append(",function2")
                    .append(",rank")
                    .append(",penalization");
            population.forEach(individual -> solventInfo
                    .append("\n" + individual.getObjectiveValues()[0])
                    .append("," + individual.getObjectiveValues()[1])
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
