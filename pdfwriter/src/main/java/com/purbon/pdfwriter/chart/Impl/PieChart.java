package com.purbon.pdfwriter.chart.Impl;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.purbon.pdfwriter.chart.AbstractChart;

public class PieChart<V> extends AbstractChart<V>  {

	@Override
	protected Dataset getDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		for (String key : keySet) {
			List<V> values = getDataRow(key);
			for (int i = 0; i < values.size(); i++) {
				result.setValue(getHeader(i), (Number)values.get(i));
			}
		}
		return result;
	}
	
	@Override
	protected JFreeChart createChart(String title, Dataset dataset) {
		JFreeChart chart = ChartFactory.createPieChart(title, (PieDataset) dataset, true, true, false);
		return chart;
	}

}
