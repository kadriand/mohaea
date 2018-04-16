package com.co.evolution.fitness;

import com.co.evolution.fitness.fnds.ParetoPlotter;
import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.individual.Individual;

import javax.swing.*;
import java.util.List;

public class ParetoPlotterInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    private int generationsGap;

    public ParetoPlotterInterceptor(int generationsGap) {
        this.generationsGap = generationsGap;
    }

    @Override
    public void apply(int generation, List<T> population) {
        if (generation % generationsGap != 0 && generation != 1)
            return;

        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population);
        paretoPlotter.pack();
        paretoPlotter.setVisible(true);
        paretoPlotter.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
