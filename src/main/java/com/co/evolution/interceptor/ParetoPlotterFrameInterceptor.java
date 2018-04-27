package com.co.evolution.interceptor;

import com.co.evolution.model.EvolutionInterceptor;
import com.co.evolution.model.ObjectiveFunction;
import com.co.evolution.model.Population;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.ParetoPlotter;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class ParetoPlotterFrameInterceptor<T extends Individual> implements EvolutionInterceptor<T> {

    private int generationsGap;
    private double[] functionsSigns;

    public ParetoPlotterFrameInterceptor(int generationsGap, ObjectiveFunction[] objectiveFunctions) {
        this.functionsSigns = new double[objectiveFunctions.length];
        for (int i = 0; i < objectiveFunctions.length; i++)
            this.functionsSigns[i] = objectiveFunctions[i].isMinimize() ? 1.0 : -1.0;
        this.generationsGap = generationsGap;
    }

    @Override
    public void apply(int generation, Population<T> population) {
        if (generation % generationsGap != 0 && generation != 1)
            return;
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Iteration " + generation, population, this.functionsSigns);
        JFrame paretoFrame = paretoPlotter.toJFrame("Plot Iteration " + generation);
        paretoFrame.setVisible(true);
        paretoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void apply(Population<T> population) {
        List<T> populationList = population.stream().filter(t -> t.getParetoRank() == 0).collect(Collectors.toList());
        ParetoPlotter<T> paretoPlotter = new ParetoPlotter<>("Last Iteration", populationList, this.functionsSigns);
        JFrame paretoFrame = paretoPlotter.toJFrame("Plot last iteration ");
        paretoFrame.setVisible(true);
        paretoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
