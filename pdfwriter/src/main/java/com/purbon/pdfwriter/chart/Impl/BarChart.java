package com.purbon.pdfwriter.chart.Impl;

import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

import com.purbon.pdfwriter.chart.AbstractChart;


public class BarChart<V> extends AbstractChart<V> {

	private static final String X_AXIS_LABEL = "";

	@Override
	protected JFreeChart createChart(String title, Dataset dataset) {
		JFreeChart chart = ChartFactory.createBarChart( title, X_AXIS_LABEL, getYAxisLabel(), 
													    (CategoryDataset) dataset, PlotOrientation.VERTICAL, 
													    false, false, false
													  );
		CategoryPlot plot 	 = (CategoryPlot) chart.getPlot();
 
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false); 
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0); 
		
		
	    return chart;
	}

}
