package com.purbon.pdfwriter.chart;

import com.purbon.pdfwriter.chart.Impl.BoxChart;
import com.purbon.pdfwriter.chart.Impl.KeyParser;
import com.purbon.pdfwriter.chart.Impl.LineChart;
import com.purbon.pdfwriter.chart.Impl.MultipleLineChart;
import com.purbon.pdfwriter.chart.Impl.PQChart;
import com.purbon.pdfwriter.chart.Impl.dicts.DefaultTranslator;

/**
 * Basic Chart factory to manage simple chart creation.
 * @author purbon
 *
 */
public class ChartFactory {

	public static MultipleLineChart createMultipleLineChart(KeyParser parser, int columnCount, String[] columnNames)  {
		return createMultipleLineChart(new DefaultTranslator(), parser, columnCount, columnNames);
	}
	
	public static MultipleLineChart createMultipleLineChart(Translator tr, KeyParser parser, int columnCount, String[] columnNames) {
		return new MultipleLineChart(tr, parser, columnCount, columnNames);
	}
	
	public static BoxChart<Float> createBoxChart() {
		return new BoxChart<Float>();
	}

	public static LineChart<Float> createLineChart() {
		return new LineChart<Float>();
	}
	public static PQChart<Float> createPQChart() {
		return new PQChart<Float>();
	}
}
