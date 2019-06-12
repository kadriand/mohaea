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
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
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
        buildPlot(name, population, new double[]{1.0, 1.0});
    }

    /**
     * Pareto fronts plotter
     *
     * @param name the frame title.
     */
    public ParetoPlotter(final String name, List<T> population, double[] functionsSigns) {
        buildPlot(name, population, functionsSigns);
    }

    private void buildPlot(String name, List<T> population, double[] functionsSigns) {
        XYSeries paretoFront = new XYSeries("First pareto front");
        XYSeries secondaryFronts = new XYSeries("Population");

        population.stream()
                .filter(individual -> individual.getParetoRank() == 0)
                .forEach(individual -> paretoFront.add(functionsSigns[0] * individual.getObjectiveValues()[0], functionsSigns[1] * individual.getObjectiveValues()[1]));
        population
                .stream().filter(individual -> individual.getParetoRank() > 0)
                .forEach(individual -> secondaryFronts.add(functionsSigns[0] * individual.getObjectiveValues()[0], functionsSigns[1] * individual.getObjectiveValues()[1]));

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(paretoFront);
        data.addSeries(secondaryFronts);
        this.scatterPlot = ChartFactory.createXYLineChart(
                name,
                "f1",
                "f2",
                data,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
        scatterPlot.getPlot().setBackgroundPaint(Color.white);

        XYPlot plot = scatterPlot.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(0, renderer);
        XYItemRenderer rendererForDataset = plot.getRendererForDataset(plot.getDataset(0));
        rendererForDataset.setSeriesPaint(0, Color.BLUE);
        rendererForDataset.setSeriesPaint(1, Color.cyan);
        rendererForDataset.setSeriesShape(0, new Ellipse2D.Double(0, 0, 4, 4));
        rendererForDataset.setSeriesShape(1, new Ellipse2D.Double(0, 0, 3, 3));
        renderer.setDefaultLinesVisible(false);
    }

    public File toFile(String name) {
        return toFile(name, Format.JPEG);
    }

    public File toFile(String name, Format format) {
        String fileName = name + "." + format.toString().toLowerCase();
        String directory = "output/";
        File scatterPlotFile = new File(directory + fileName);
        scatterPlotFile.getParentFile().mkdirs();

        try {
            int width = 640;   /* Width of the image */
            int height = 480;  /* Height of the image */
            if (format == Format.JPEG)
                ChartUtils.saveChartAsJPEG(scatterPlotFile, scatterPlot, width, height);
            else if (format == Format.SVG) {
                SVGGraphics2D svgGraphic = new SVGGraphics2D(width, height);
                scatterPlot.draw(svgGraphic, new Rectangle(0, 0, width, height));
                SVGUtils.writeToSVG(scatterPlotFile, svgGraphic.getSVGElement());
            }
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

    public static enum Format {
        JPEG, SVG
    }
}