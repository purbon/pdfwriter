package com.purbon.pdfwriter.chart.Impl;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import com.purbon.pdfwriter.chart.AbstractChart;

public class LineChart<V> extends AbstractChart<V>  {

	private static final String X_AXIS_LABEL = "";

	@Override
	protected Dataset getDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		for (String key : keySet) {
			List<V> values = getDataRow(key);
			for (int i = 0; i < values.size(); i++) {
				result.addValue((Number) values.get(i), getHeader(i), key);
			}
		}
		return result;
	}

	@Override
	protected JFreeChart createChart(String title, Dataset dataset) {

		JFreeChart chart = ChartFactory.createStackedAreaChart(title, X_AXIS_LABEL, getYAxisLabel(), (CategoryDataset) dataset, 
				                                                 PlotOrientation.VERTICAL, true, true, false );

		String fontName = chart.getCategoryPlot().getDomainAxis().getTickLabelFont().getName();
		setFont(chart, fontName, 6);
		configure(chart);
		
		return chart;
	}

	private void configure(JFreeChart chart) {
		CategoryPlot p = chart.getCategoryPlot();
		p.setForegroundAlpha(0.5f);
		p.setBackgroundPaint(Color.lightGray);
		p.setDomainGridlinesVisible(true);
		p.setDomainGridlinePaint(Color.white);
		p.setRangeGridlinesVisible(true);
		p.setRangeGridlinePaint(Color.white);
		
		CategoryItemRenderer renderer = p.getRenderer();
		renderer.setSeriesPaint(1, new Color(16,165,74));
		renderer.setSeriesPaint(2, new Color(40,234,115));

	}

	private void setFont(JFreeChart chart, String name, int size) {
		Font font = new Font(name, Font.PLAIN, size);
		CategoryPlot p = chart.getCategoryPlot();
		ValueAxis vaxis = p.getRangeAxis();
		vaxis.setTickLabelFont(font);
		CategoryAxis caxis = p.getDomainAxis();
		caxis.setTickLabelFont(font);
	}

}
