package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.GeneticOperator;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class OperatorsRatesInterceptor<T extends Individual> extends EvolutionInterceptor<T> {

    private String fieldSeparator = ",";
    private String textExtension = "csv";
    private String[] geneticOperators;
    private File ratesFile;

    public OperatorsRatesInterceptor(String ratesPathPrefix, List<GeneticOperator<T>> geneticOperators) {
        init(ratesPathPrefix, geneticOperators);
    }

    public OperatorsRatesInterceptor(String ratesPathPrefix, List<GeneticOperator<T>> geneticOperators, String textExtension, String fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
        this.textExtension = textExtension;
        init(ratesPathPrefix, geneticOperators);
    }

    private void init(String ratesPathPrefix, List<GeneticOperator<T>> geneticOperators) {
        try {
            this.geneticOperators = geneticOperators.stream().map(p -> p.getClass().getSimpleName()).toArray(String[]::new);
            this.ratesFile = new File(String.format("output/%srates.%s", ratesPathPrefix, textExtension));
            ratesFile.getParentFile().mkdirs();
            StringBuilder ratesHeader = new StringBuilder("Generation");
            for (String geneticOperator : this.geneticOperators)
                ratesHeader.append(fieldSeparator).append(geneticOperator);
            Files.write(ratesFile.toPath(), ratesHeader.toString().getBytes());
            System.out.println("Rates file stored in " + ratesFile.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void apply(Population<T> population, int generation, Map<T, double[]> generationOperatorsRates) {

        double[] operatorsRatesMeans = new double[this.geneticOperators.length];
        for (T individual : population)
            for (int i = 0; i < operatorsRatesMeans.length; i++)
                operatorsRatesMeans[i] += generationOperatorsRates.get(individual)[i];
        double ratesSum = DoubleStream.of(operatorsRatesMeans).sum();
        for (int i = 0; i < operatorsRatesMeans.length; i++)
            operatorsRatesMeans[i] /= ratesSum;

        System.out.println("Generation: " + generation + ". Best Individual: " + population.getBest().toString() +
                ". Best Fitness: " + population.getBest().getFitness() +
                ". Best Objective Functions: " + Arrays.toString(population.getBest().getObjectiveValues()) +
                ". Rates means: " + Arrays.toString(operatorsRatesMeans));

        try {
            StringBuilder ratesMeans = new StringBuilder("\n" + generation);
            for (double operatorsRates : operatorsRatesMeans)
                ratesMeans.append(fieldSeparator).append(operatorsRates);
            Files.write(ratesFile.toPath(), ratesMeans.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
