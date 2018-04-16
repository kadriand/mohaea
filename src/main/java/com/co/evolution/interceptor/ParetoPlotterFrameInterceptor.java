package com.co.evolution.interceptor;

import com.co.evolution.util.ParetoPlotter;
import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.individual.Individual;

import javax.swing.*;
import java.util.List;

public class ParetoPlotterFrameInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    private int generationsGap;

    public ParetoPlotterFrameInterceptor(int generationsGap) {
        this.generationsGap = generationsGap;
    }

    @Override
    public void apply(int generation, List<T> population) {
        if (generation % generationsGap != 0 && generation != 1)
            return;
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population);
        JFrame paretoFrame = paretoPlotter.toJFrame("Plot Iteration " + generation);
        paretoFrame.setVisible(true);
        paretoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
