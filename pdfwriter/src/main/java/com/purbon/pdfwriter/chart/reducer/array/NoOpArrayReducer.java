package com.purbon.pdfwriter.chart.reducer.array;

import java.util.List;

public class NoOpArrayReducer extends ArrayReducer {

	private String [] headers;
	
	public NoOpArrayReducer() {
		headers = new String[0];
	}
	public String[] headers() {
		return headers;
	}

	public String[] headers(String[] headers) {
		return headers;
	}

	@Override
	public List<Float> reduce(List<Float> data) {
		return data;
	}


}
