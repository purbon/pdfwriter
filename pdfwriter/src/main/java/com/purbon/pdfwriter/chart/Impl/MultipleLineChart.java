package com.purbon.pdfwriter.chart.Impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import com.purbon.pdfwriter.chart.Translator;
import com.purbon.pdfwriter.chart.Impl.KeyParser.KeyParts;
import com.purbon.pdfwriter.chart.Impl.dicts.DefaultTranslator;
import com.purbon.pdfwriter.chart.extensions.jfreechart.NumberAxisWithUnits;

public class MultipleLineChart extends LineChart<Float> {
	
	private Map<String, String> dictionary;
	private Translator tr;
	private int columnCount;
	private int columnSize;
	private String [] columnNames;
	private KeyParser parser;
	
	private static Logger log = Logger.getLogger(MultipleLineChart.class.getName());

	public MultipleLineChart() {
		this(new DefaultTranslator(), null, -1, new String[]{});
	}
	
	public MultipleLineChart(KeyParser parser, int columnCount, String[] columnNames) {
		this(new DefaultTranslator(), parser, columnCount, columnNames);
	}
	
	public MultipleLineChart(Translator tr, KeyParser parser, int columnCount, String[] columnNames) {
		super();
		this.tr          = tr;
		this.columnCount = columnCount;
		this.columnNames = columnNames;
		this.columnSize  = -1;
		this.parser      = parser;
		
	}

	@Override
	protected JFreeChart createChart(String title, Dataset dataset) {
		if (parser == null)
			throw new IllegalArgumentException("No key parser configured.");
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "", 	getYAxisLabel(), null, true, true, false);
		XYPlot plot = (XYPlot) chart.getPlot();	
		Map<String, YIntervalSeriesCollection> map = new HashMap<String, YIntervalSeriesCollection>();
		List<String> keySet = new ArrayList<String>();
		
		if (columnCount < 0) {
			columnCount = estimateColumnCount()/4;
		}
		
		try {
			keySet = flushColumnCaseData(map);
		} catch (IndexOutOfBoundsException ex) {
			log.error("Incorrect column configuration", ex);
			throw new IllegalArgumentException(ex);
		}
			
		int i=0;
		Color[] colors = new Color[]{new Color(255, 200, 200), new Color(200, 200, 255) };
		for(String key : keySet) {
		
			YIntervalSeriesCollection collection = map.get(key);
			plot.setDataset(i, collection);
			
			DeviationRenderer renderer = new DeviationRenderer(true, false);
			
			Stroke s = new BasicStroke(3.0f);
			renderer.setSeriesStroke(i, s);
			renderer.setSeriesFillPaint(i%2, colors[i%2]);
			
			plot.setRenderer(i, renderer);
			NumberAxis axis = buildNumberAxis(key);
		
			axis.setTickLabelsVisible(true);
			axis.setAutoRangeIncludesZero(false);
			
			if (i > 0)
				plot.setRangeAxis(i, axis);
			plot.mapDatasetToRangeAxis(i,i);
		    
			i++;
		}

		return chart;
	}
	
	private List<String> flushColumnCaseData(Map<String, YIntervalSeriesCollection> map) {
			
		Map<String, YIntervalSeries> intervalSeries = new HashMap<String, YIntervalSeries>();
		List<String> names = new ArrayList<String>();
		
		for (String key : keySet) {
			log.debug(key);
			KeyParts parts = null;
			try {
				parts = parser.parse(key);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Incorrect key format, check your parser");
			}
			if (parts.unit.equalsIgnoreCase(parts.elem)) {
				for(String columnName : columnNames) {
					if (intervalSeries.get(columnName) == null) {
						intervalSeries.put(columnName, new YIntervalSeries(columnName));
						names.add(columnName);
					}
				}
			} else {
				if (intervalSeries.get(parts.unit) == null) {
					names.add(parts.unit);
					intervalSeries.put(parts.unit, new YIntervalSeries(tr.translateWithUnits(parts.unit)));
				}
			}
		}
		for(String key : keySet) {
			List<Float> values = getDataRow(key);
			KeyParts parts = parser.parse(key);
			String date = parts.timeString;
			if (parts.unit.equalsIgnoreCase(parts.elem)) {
				date = key;
			}
			int block_size = getColumnSize();
			int j          = 0;
			for(int i=0; i < values.size();) {
				Map<String, Float> c = grabData(values, i, i+block_size);	
				try {
					flushSeries(intervalSeries.get(names.get(j)), c, date);
				} catch (IndexOutOfBoundsException ex) {
					throw new IllegalArgumentException("Incorrect configuration, missing column names.");
				}
				j++;
				i+=block_size;
			}
  			
		}
		List<String> keys = new ArrayList<String>();
		for(String name : names) {
			YIntervalSeriesCollection result = new YIntervalSeriesCollection();
			result.addSeries(intervalSeries.get(name));
			map.put(name, result);
			keys.add(name);
		}
						
		return keys;
	}
	
	/*private List<String> flushDoubleColumnCase(Map<String, YIntervalSeriesCollection> map) {
  			
		YIntervalSeries seriesA = null, seriesB = null;
		String units = "";
		log.debug("");
		for (String key : keySet) {
			log.debug(key);
			List<Float> values = data.get(key);
			String date = "";
			
			try {
				String elem = key.split("#")[0];
				       date = key.split("#")[2];
				String unit = elem.split("\\(")[1].replace(")","").toUpperCase();
  			
				if (seriesA == null && seriesB == null) {
					seriesA = new YIntervalSeries(tr.translateWithUnits(unit));
				}

				if (seriesB == null && "I".equalsIgnoreCase(unit)) {
					seriesB = new YIntervalSeries(tr.translateWithUnits("U"));
					units   = "U#"+unit;
				} else if (seriesB == null && "P".equalsIgnoreCase(unit)) {
					seriesB = new YIntervalSeries(tr.translate("Q"));
					units   = unit+"#Q";
				}
			} catch (Exception ex) {
				date = key;
				if (seriesA == null)
					seriesA = new YIntervalSeries("Humidity (%)");
				if (seriesB == null)
					seriesB = new YIntervalSeries("Temperature (°C)");
				units   = "Humidity#Temperature";
			}
  			
  			Map<String, Float> c = grabData(values, 0, 3);
  			flushSeries(seriesA, c, date);
  			c = grabData(values, 3, 6);
  			flushSeries(seriesB, c, date);
		}
		YIntervalSeriesCollection result = new YIntervalSeriesCollection();
		List<String> keys = new ArrayList<String>();
		String[] c = units.split("#");
		if (seriesA != null) {
			result.addSeries(seriesA);
			map.put(c[0], result);
			keys.add(c[0]);
		}
		
		result = new YIntervalSeriesCollection();

		if (seriesB != null) {
			result.addSeries(seriesB);
			map.put(c[1], result);
			keys.add(c[1]);
		}
		log.debug("");
		return keys;
	}*/
	
	//  jdl04(I)##2/4/2014
	//  23:31%2/4/2014
	private void flushSeries(YIntervalSeries series, Map<String, Float> c, String date) {
		Day day = null;
		if (c.size() > 2) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date myDate = sdf.parse(date);
				day = new Day(myDate);
				series.add(day.getFirstMillisecond(), c.get("AVG"), c.get("MIN"), c.get("MAX"));
			} catch (ParseException e) {
				String shour = date.split("%")[0];
				       date = date.split("%")[1];
				Date myDate;
				try {
					myDate = sdf.parse(date);
				} catch (ParseException e1) {
					myDate = new Date();
				}
				int myHour = Integer.valueOf(shour.split(":")[0]);
				Hour hour = new Hour(myHour, new Day(myDate));
				int myMinute = Integer.valueOf(shour.split(":")[1]);
				Minute min = new Minute(myMinute, hour);
				series.add(min.getFirstMillisecond(), c.get("AVG"), c.get("MIN"), c.get("MAX"));
			}
		}
	}
		
	
	private Map<String, Float> grabData(List<Float> values, int i0, int in) {
		Map<String, Float> c = new HashMap<String, Float>();
		for (int i = i0; i < in; i++) {
			String aggregate = getHeader(i);
			if (!"StdDev".equalsIgnoreCase(aggregate)) {
				c.put(aggregate, values.get(i));
			}
		}
		return c;
	}
	
	public void setDictionary(Map<String, String> dictionary) {
		this.dictionary = dictionary;
	}
	
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getColumnSize() {
		if (columnSize < 0) {
			return this.getHeadersCount()/columnCount;
		} 
		return columnSize;
	}
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	private NumberAxis buildNumberAxis(String key) {
		NumberAxis axis = null;
		if (dictionary == null || (dictionary != null && dictionary.get(key) == null))
			axis = new NumberAxisWithUnits(tr.translateWithUnits(key));
		else if (dictionary.get(key) != null)
			axis = new NumberAxisWithUnits(tr.translateWithUnits(key),dictionary.get(key));
		return axis;
	}
	
	public void setKeyParser(KeyParser parser) {
		this.parser = parser;
	}
	
	@Override
	protected Dataset getDataset() {
		return null;
	}
	
}
