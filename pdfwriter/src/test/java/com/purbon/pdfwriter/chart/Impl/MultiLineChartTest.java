package com.purbon.pdfwriter.chart.Impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.chart.JFreeChart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.chart.ChartFactory;
import com.purbon.pdfwriter.chart.Impl.MultipleLineChart;
import com.purbon.pdfwriter.chart.Impl.keys.SensorUnitKeyParser;
import com.purbon.pdfwriter.chart.Impl.keys.TimeKeyParser;

import test.support.Keys;
import test.support.RandomKeys;

public class MultiLineChartTest {

	private MultipleLineChart chart;

	public MultiLineChartTest() {
	}
	
	@Before
	public void setUp() throws Exception {
		chart = ChartFactory.createMultipleLineChart(new TimeKeyParser(), 1, new String[]{});
	}

	@After
	public void tearDown() throws Exception {
	
	}
	
	@Test
	public void testImproverKeyFormat() {
		chart.setColumnCount(1);
		chart.setHeaders(new String[]{"MIN", "AVG", "StdDev", "MAX"});
		RandomKeys keys = new RandomKeys(100);
		while(keys.hasNext()) {
			String key = keys.next();
			chart.insertData(key, randomList(4));
		}
		boolean thrown = false;
		try {
			chart.createChart("testImproverKeyFormat");
			thrown = false;
		} catch (Exception ex) {
			thrown = true;
		}
		assertTrue(thrown);
		}
	
	@Test
	public void testMissingColumnNames() throws IOException {
		chart.setColumnCount(1);
		chart.setHeaders(new String[]{"MIN", "AVG", "StdDev", "MAX"});		
		for(int i=0; i < 10; i++) {
			int day = 10+i;
			Keys keys = new Keys(day+"/12/2014");
			while(keys.hasNext()) {
				String key = keys.next();
				chart.insertData(key, randomList(4));
			}
		}
		boolean thrown = false;
		try {
			chart.createChart("testMissingColumnNames");
			thrown = false;
		} catch (Exception ex) {
			thrown = true;
		}
		assertTrue(thrown);
	
	}
	@Test
	public void testWrongNumberOfColumns() {
		chart.setColumnCount(2);
		chart.setHeaders(new String[]{"MIN", "AVG", "StdDev", "MAX"});
		Keys keys = new Keys("03/12/2014");
		while(keys.hasNext()) {
			String key = keys.next();
			chart.insertData(key, randomList(4));
		}
		boolean thrown = false;
		try {
			chart.createChart("testWrongNumberOfColumns");
			thrown = false;
		} catch (IllegalArgumentException ex) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	@Test
	public void testOneColumnSize() {
		chart.setColumnCount(1);
		chart.setHeaders(new String[]{"MIN", "AVG", "StdDev", "MAX"});
		chart.setKeyParser(new SensorUnitKeyParser());
		Keys keys = new Keys(9, 4, "03/12/2014");
		while(keys.hasNext()) {
			String key = keys.next();
			chart.insertData(key, randomList(4));
		}
		JFreeChart myObj = chart.createChart("demo");
		assertNotNull(myObj);
	}
	
	@Test
	public void testEstimateColumnCount() {
		chart.setColumnCount(-1);
		chart.setHeaders(new String[]{"MIN", "AVG", "StdDev", "MAX"});
		chart.setKeyParser(new SensorUnitKeyParser());
		Keys keys = new Keys(9, 4, "03/12/2014");
		while(keys.hasNext()) {
			String key = keys.next();
			chart.insertData(key, randomList(4));
		}
		JFreeChart myObj = chart.createChart("demo");
		assertNotNull(myObj);
	}

	@Test
	public void testDoubleColumnBuild() {
		chart.setColumnCount(2);
		chart.setColumnNames(new String[]{"Humidity", "Temperature"});
		chart.setHeaders(new String[]{"MIN", "AVG", "StdAvg", "MAX", "MIN", "AVG", "STDAVG", "MAX"});
		Keys keys = new Keys("03/12/2014");
		while(keys.hasNext()) {
			String key = keys.next();
			chart.insertData(key, randomList(8));
		}

		JFreeChart myObj = chart.createChart("demo");
		assertNotNull(myObj);
	}
	
	@Test
	public void testDoubleColumnSize() {
		chart.setColumnCount(2);
		chart.setColumnNames(new String[]{"Humidity", "Temperature"});
		chart.setHeaders(new String[]{"MIN", "AVG", "StdAvg", "MAX", "MIN", "AVG", "STDAVG", "MAX"});
		Keys keys = new Keys("03/12/2014");
		while(keys.hasNext()) {
			String key = keys.next();
			chart.insertData(key, randomList(8));
		}
		JFreeChart myObj = chart.createChart("demo");
		assertNotNull(myObj);
	}

	private List<Float> randomList(int size) {
		List<Float> list = new ArrayList<Float>();
		Random rand = new Random(System.currentTimeMillis());
		for(int i=0; i < size; i+=4) {
			float max = rand.nextFloat();
			float min = max/2;
			float avg = ((max-min)/2)+min;
			list.add(min);
			list.add(avg);
			list.add(avg);
			list.add(max);
		}
		return list;
	}
}
