package com.co.evolution.util;


import com.co.evolution.model.individual.Individual;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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


public class ParetoPlotter<T extends Individual> {

    private JFreeChart scatterPlot;

    /**
     * Pareto fronts plotter
     *
     * @param name the frame title.
     */
    public ParetoPlotter(final String name, List<T> population) {
        buildPlot(name, population);
    }

    private void buildPlot(String name, List<T> population) {
        XYSeries paretoFront = new XYSeries("First pareto front");
        XYSeries secondaryFronts = new XYSeries("Population");

        population.stream()
                .filter(individual -> individual.getHowManyDominateMe() == 0)
                .forEach(individual -> paretoFront.add(individual.getObjectiveFunctionValues()[0], individual.getObjectiveFunctionValues()[1]));
        population
                .stream().filter(individual -> individual.getHowManyDominateMe() > 0)
                .forEach(individual -> secondaryFronts.add(individual.getObjectiveFunctionValues()[0], individual.getObjectiveFunctionValues()[1]));

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(paretoFront);
        data.addSeries(secondaryFronts);
        this.scatterPlot = ChartFactory.createScatterPlot(
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
    }

    public File toFile(String name) {
        String fileName = name + ".jpeg";
        String directory = "output/";
        File scatterPlotFile = new File(directory + fileName);
        if (!scatterPlotFile.exists())
            new File(directory).mkdir();

        try {
            int width = 640;   /* Width of the image */
            int height = 480;  /* Height of the image */
            ChartUtils.saveChartAsJPEG(scatterPlotFile, scatterPlot, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scatterPlotFile;
    }

    public JFrame toJFrame(String title) {
        ChartPanel chartPanel = new ChartPanel(scatterPlot);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        JFrame frame = new JFrame(title);
        frame.setContentPane(chartPanel);
        frame.pack();
        return frame;
    }

}