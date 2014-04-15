package com.purbon.pdfwriter.chart.Impl;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.purbon.pdfwriter.chart.AbstractChart;
import com.purbon.pdfwriter.chart.extensions.jfreechart.PQDiagramm;

public class PQChart<V> extends AbstractChart<V> {

	@Override
	@SuppressWarnings("unchecked")
	protected Dataset getDataset() {
		XYSeriesCollection result = new XYSeriesCollection();
		XYSeries series = new XYSeries("P-Q");
		for (String key : keySet) {
			List<Float> values = (List<Float>) getDataRow(key);
			if (values.size() == 2) {
				XYDataItem item = new XYDataItem(values.get(0), values.get(1));
				series.add(item);
			}
		}
		result.addSeries(series);
		return result;
	}
	
	@Override
	protected JFreeChart createChart(String title, Dataset dataset) {
		PQDiagramm chart = PQDiagramm.createScatterPlot(title, "P in MW", "Q in Mvar", (XYDataset) dataset, PlotOrientation.VERTICAL, false, false, false);
		
		XYPlot plot = chart.getXYPlot();
		XYItemRenderer xy = plot.getRenderer(); 
		xy.setSeriesPaint(0,new Color(0,0, 255, 100)); 
		xy.setSeriesShape(0, new Ellipse2D.Double(0,0,2,2)); 
			
		return chart;
	}

}
