package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;

import java.io.File;
import java.util.List;

public class ParetoPlotterImageInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    private int generationsGap;

    public ParetoPlotterImageInterceptor(int generationsGap) {
        this.generationsGap = generationsGap;
    }

    @Override
    public void apply(int generation, List<T> population) {
        if (generation % generationsGap != 0 && generation != 1)
            return;
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population);
        File scatterPlotFile = paretoPlotter.toFile("iteration-" + generation);
        try {
            System.out.println("Pareto fronts stored in " + scatterPlotFile.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
