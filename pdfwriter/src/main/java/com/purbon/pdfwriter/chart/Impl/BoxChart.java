package com.purbon.pdfwriter.chart.Impl;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import com.purbon.pdfwriter.chart.AbstractChart;


public class BoxChart<V> extends AbstractChart<V> {

	private static final String X_AXIS_LABEL = "";

	protected Dataset getDataset() {
		DefaultBoxAndWhiskerCategoryDataset result = new DefaultBoxAndWhiskerCategoryDataset();
		
		for (String key : keySet) {
			List<V> values = getDataRow(key);
			Number min = (Number)values.get(0);
			Number avg = (Number)values.get(1);
			Number stddev = (Number)values.get(2);
			Number max = (Number)values.get(3);

			BoxAndWhiskerItem item = new BoxAndWhiskerItem(avg, avg, avg.doubleValue()-stddev.doubleValue(), avg.doubleValue()+stddev.doubleValue(), min, max, min, max, null);
			result.add(item, key, key);
		}
		return result;
	}

	@Override
	protected JFreeChart createChart(String title, Dataset dataset) {
	 
	   JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(title, X_AXIS_LABEL, getYAxisLabel(), (BoxAndWhiskerCategoryDataset) dataset, false);
	   
	   CategoryPlot p = chart.getCategoryPlot();
	   p.setForegroundAlpha(0.5f);
	   p.setBackgroundPaint(Color.lightGray);
	   p.setDomainGridlinesVisible(true);
	   p.setDomainGridlinePaint(Color.white);
	   p.setRangeGridlinesVisible(true);
	   p.setRangeGridlinePaint(Color.white);

	   BoxAndWhiskerRenderer renderer = (BoxAndWhiskerRenderer)p.getRenderer();
	   renderer.setSeriesPaint(0, new Color(40,234,115));
	   renderer.setMeanVisible(false);
	   
	   return chart;
	}

}
