package com.purbon.pdfwriter.chart;

import java.util.List;

import org.jfree.chart.JFreeChart;

public interface Chart<V> {
	
	public void setHeaders(String[] headers);
	public void insertData(String key, List<V> row);
	
	public JFreeChart createChart(String title);
	
	public void setYAxisLabel(String string);
	public String getYAxisLabel();
}
