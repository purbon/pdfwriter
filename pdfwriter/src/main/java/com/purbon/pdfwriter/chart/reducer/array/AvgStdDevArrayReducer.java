package com.purbon.pdfwriter.chart.reducer.array;

import java.util.ArrayList;
import java.util.List;

public class AvgStdDevArrayReducer extends ArrayReducer {

	private String[] headers;
	
	public AvgStdDevArrayReducer() {
		headers = new String[]{"Avg-StdDev", "Avg", "Avg+StdDev"};
	}
	
	public String[] headers() {
		return headers;
	}

	public String[] headers(String[] headers) {
		return this.headers;
	}

 	public List<Float> reduce(List<Float> data) {
 		
		Float avg   = data.get(1);
		Float stdev = data.get(2);
		
		List<Float> reduced = new ArrayList<Float>();
		reduced.add(avg-stdev);
		reduced.add(avg);
		reduced.add(avg+stdev);
		
		return reduced;
		
 	}

}
