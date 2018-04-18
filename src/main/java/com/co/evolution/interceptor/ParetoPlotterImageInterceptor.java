package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;

import java.io.File;
import java.util.List;

public class ParetoPlotterImageInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    private int generationsGap;
    private String filePrefix;

    public ParetoPlotterImageInterceptor(int generationsGap, String prefix) {
        this.generationsGap = generationsGap;
        this.filePrefix = prefix;
    }

    @Override
    public void apply(int generation, List<T> population) {
        if (generation % generationsGap != 0 && generation != 1)
            return;
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population);
        File scatterPlotFile = paretoPlotter.toFile(filePrefix + "iteration-" + generation);
        try {
            System.out.println("Pareto fronts stored in " + scatterPlotFile.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
