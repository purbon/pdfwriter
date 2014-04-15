package com.purbon.pdfwriter.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

public abstract class AbstractChart<V> implements Chart<V> {

	private Map<String, List<V>> data;
	protected List<String> keySet;
 	protected String [] headers;
 	
 	private String yAxisLabel;

	public AbstractChart() {
		data   = new HashMap<String, List<V>>();
		keySet = new ArrayList<String>();
		yAxisLabel = "";
	}
	
	public List<V> getDataRow(String key) {
		return data.get(key);
	}
	
	public void insertData(String key, List<V> value) {
 		data.put(key, value);
 		keySet.add(key);
	}
	
	protected int estimateColumnCount() {
		int size = -1;
		for(String key : keySet) {
			int rowSize = data.get(key).size();
			if (size != -1) {
				if (size != rowSize) {
					throw new IllegalArgumentException("Different column count");
				}
			}
			size = rowSize;
		}
		return size;
	}
	
	public void setHeaders(String[] headers) {
  		this.headers = headers;
	}
	
	public int getHeadersCount() {
		if (headers == null)
			return -1;
		return headers.length;
	}
	
	public String getHeader(int index) {
		if (headers == null)
			return "value "+index;
		return headers[index];
	}

	public JFreeChart createChart(String title) {
		return createChart(title, getDataset());
	}
	
	protected abstract JFreeChart createChart(String title, Dataset dataset);

	protected Dataset getDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		for (String key : keySet) {
			List<V> values = data.get(key);
			for (int i = 0; i < values.size(); i++) {
				result.addValue((Number) values.get(i), key, getHeader(i));
			}
		}
		return result;
	}

	public void setYAxisLabel(String label) {
		yAxisLabel = label;
	}

	public String getYAxisLabel() {
		return yAxisLabel;
	}
	
	

}
