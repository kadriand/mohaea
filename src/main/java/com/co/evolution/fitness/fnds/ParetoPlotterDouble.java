package com.co.evolution.fitness.fnds;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;


public class ParetoPlotterDouble extends JFrame {

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title the frame title.
     */
    public ParetoPlotterDouble(final String title, double[][] individuals, int[] ranks) {
        super(title);
        final XYSeries series = new XYSeries("Random Data");
        final XYSeries series2 = new XYSeries("Random Data 2");
        for (int i = 0; i < individuals.length; i++)
            if (ranks[i] == 0)
                series2.add(individuals[i][0], individuals[i][1]);
            else
                series.add(individuals[i][0], individuals[i][1]);

        final XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(series);
        data.addSeries(series2);
        final JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                "XY Series Demo",
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        scatterPlot.getPlot().setBackgroundPaint(Color.white);





        final ChartPanel chartPanel = new ChartPanel(scatterPlot);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);

    }

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

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {

        final ParetoPlotterDouble demo = new ParetoPlotterDouble("XY Series Demo",null,null);
        demo.pack();
        demo.setVisible(true);
        demo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}