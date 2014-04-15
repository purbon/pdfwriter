package com.purbon.pdfwriter.chart.reducer.array;

import java.util.List;

import com.purbon.pdfwriter.chart.reducer.Reducer;

public abstract class ArrayReducer implements Reducer {

	public abstract List<Float>  reduce(List<Float> data);
	
	private String [] headers;
	
	public ArrayReducer() {
		headers = new String[0];
	}
	public String[] headers() {
		return headers;
	}

	public String[] headers(String[] headers) {
		return headers;
	}
}
