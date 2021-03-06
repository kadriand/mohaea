package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

public class ParetoPlotterImageInterceptor<T extends Individual> extends EvolutionInterceptor<T> {

    private String imagesPathPrefix;
    private double[] functionsSigns;
    @Setter
    private String fieldSeparator = ",";
    @Setter
    private String textExtension = "csv";
    @Setter
    private ParetoPlotter.Format format = ParetoPlotter.Format.JPEG;

    public ParetoPlotterImageInterceptor(int generationsGap, String prefix, ObjectiveFunction... objectiveFunctions) {
        this.functionsSigns = new double[objectiveFunctions.length];
        for (int i = 0; i < objectiveFunctions.length; i++)
            this.functionsSigns[i] = objectiveFunctions[i].isMinimize() ? 1.0 : -1.0;
        this.generationsGap = generationsGap;
        this.imagesPathPrefix = prefix;
    }

    @Override
    public void apply(Population<T> population, int generation, Map<T, double[]> operatorsRates) {
        System.out.println("Value: " + population.getBest().toString() + " Fitness: " + population.getBest().getFitness() + " Value: " + Arrays.toString(population.getBest().getObjectiveValues()));
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Generation " + generation, population, this.functionsSigns);
        File scatterPlotFile = paretoPlotter.toFile(imagesPathPrefix + "generation-" + generation, format);

        try {
            System.out.println("Pareto fronts stored in " + scatterPlotFile.getCanonicalPath());

            File pointsFile = new File(String.format("output/%sgeneration-%s.%s", imagesPathPrefix, generation, textExtension));
            pointsFile.getParentFile().mkdirs();
            StringBuilder generationInfo = new StringBuilder();
            generationInfo.append(String.format("individual%1$sobjective1%1$sobjective2%1$sfitness%1$srank%1$spenalization", fieldSeparator));
            population.forEach(individual -> generationInfo
                    .append(String.format("\n%s%s%s%2$s%s%2$s%s%2$s%s%2$s%s",
                            individual.toString(), fieldSeparator,
                            individual.getObjectiveValues()[0],
                            individual.getObjectiveValues()[1],
                            individual.getFitness(),
                            individual.getParetoRank(),
                            individual.getPenalization()
                    )));
            Files.write(pointsFile.toPath(), generationInfo.toString().getBytes());
            System.out.println("Pareto points stored in " + pointsFile.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
