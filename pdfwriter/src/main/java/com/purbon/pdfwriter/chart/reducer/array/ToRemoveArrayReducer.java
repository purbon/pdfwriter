package com.purbon.pdfwriter.chart.reducer.array;

import java.util.List;

public class ToRemoveArrayReducer extends ArrayReducer {

 	
	public ToRemoveArrayReducer() {
		super();
	}
 
	@Override
	public List<Float> reduce(List<Float> data) {
		data.remove(2);
		data.remove(1);
		return data;
	}

}
