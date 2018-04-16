package com.co.evolution.fitness.fnds;


import com.co.evolution.model.individual.Individual;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// ****************************************************************************
// * JFREECHART DEVELOPER GUIDE                                               *
// * The JFreeChart Developer Guide, written by David Gilbert, is available   *
// * to purchase from Object Refinery Limited:                                *
// *                                                                          *
// * http://www.object-refinery.com/jfreechart/guide.html                     *
// *                                                                          *
// * Sales are used to provide funding for the JFreeChart project - please    *
// * support us so that we can continue developing free software.             *
// ****************************************************************************


public class ParetoPlotter<T extends Individual> extends JFrame {

    /**
     * Pareto fronts plotter
     *
     * @param name the frame title.
     */
    public ParetoPlotter(final String name, List<T> population) {
        super("Plot " + name);
        final XYSeries paretoFront = new XYSeries("First pareto front");
        final XYSeries secondaryFronts = new XYSeries("Population");

        population.stream()
                .filter(individual -> individual.getHowManyDominateMe() == 0)
                .forEach(individual -> paretoFront.add(individual.getObjectiveFunctionValues()[0], individual.getObjectiveFunctionValues()[1]));
        population
                .stream().filter(individual -> individual.getHowManyDominateMe() > 0)
                .forEach(individual -> secondaryFronts.add(individual.getObjectiveFunctionValues()[0], individual.getObjectiveFunctionValues()[1]));

        final XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(paretoFront);
        data.addSeries(secondaryFronts);
        final JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                name,
                "f1",
                "f2",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        scatterPlot.getPlot().setBackgroundPaint(Color.white);

        XYPlot plot = scatterPlot.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(0, renderer);
        XYItemRenderer rendererForDataset = plot.getRendererForDataset(plot.getDataset(0));
        rendererForDataset.setSeriesPaint(0, Color.BLUE);
        rendererForDataset.setSeriesPaint(1, Color.GREEN);
        renderer.setDefaultLinesVisible(false);

        final ChartPanel chartPanel = new ChartPanel(scatterPlot);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }


}