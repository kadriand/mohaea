package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;

import javax.swing.*;

public class ParetoPlotterFrameInterceptor<T extends Individual> extends EvolutionInterceptor<T> {

    private double[] functionsSigns;

    public ParetoPlotterFrameInterceptor(int generationsGap, ObjectiveFunction[] objectiveFunctions) {
        this.functionsSigns = new double[objectiveFunctions.length];
        for (int i = 0; i < objectiveFunctions.length; i++)
            this.functionsSigns[i] = objectiveFunctions[i].isMinimize() ? 1.0 : -1.0;
        this.generationsGap = generationsGap;
    }

    @Override
    public void apply(Population<T> population, int generation) {
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population, this.functionsSigns);
        JFrame paretoFrame = paretoPlotter.toJFrame("Plot Iteration " + generation);
        paretoFrame.setVisible(true);
        paretoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
