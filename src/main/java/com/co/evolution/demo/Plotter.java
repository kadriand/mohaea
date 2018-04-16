package com.co.evolution.demo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;


/**
 * Hello world!
 */
public class Plotter {
    public static void main(String[] args) {
        System.out.println("Visualization of Pareto Sets");


//        System.out.println("Generate Random Entries");
//        List<Entry> randomEntries = new ArrayList<>();
//
//        Random r = new Random();
//        int rangeMin = 10;
//        int rangeMax = 90;
//        for (int i = 0; i < 40; i++) {
//            double value1 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
//            double value2 = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
//            randomEntries.add(new Entry(value1, value2));
//        }
//
//        for (Entry e : randomEntries) {
//            System.out.println(e.toString());
//        }


        System.out.println("Sample XY Plot");
        final XYSeries firefox = new XYSeries("Firefox");
        firefox.add(1.0, 1.0);
        firefox.add(2.0, 4.0);
        firefox.add(3.0, 3.0);

        final XYSeries chrome = new XYSeries("Chrome");
        chrome.add(1.0, 4.0);
        chrome.add(2.0, 5.0);
        chrome.add(3.0, 6.0);

        final XYSeries iexplorer = new XYSeries("InternetExplorer");
        iexplorer.add(3.0, 4.0);
        iexplorer.add(4.0, 5.0);
        iexplorer.add(5.0, 4.0);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(firefox);
        dataset.addSeries(chrome);
        dataset.addSeries(iexplorer);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Browser usage statastics",
                "Category",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        xylineChart.getPlot().setBackgroundPaint(Color.white);


        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        String fileName = "//XYChart.jpeg";
        String folder = "output";
        String filePath = folder + fileName;
        File XYChart = new File(filePath);
        if (!XYChart.exists()) {
            File dir = new File(folder);
            dir.mkdir();
        }
        try {
        System.out.println("file in " + XYChart.getCanonicalPath());
            ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
