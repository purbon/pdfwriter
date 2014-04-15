package com.purbon.pdfwriter.chart.Impl;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.chart.ChartFactory;
import com.purbon.pdfwriter.chart.Impl.LineChart;

public class LineChartTest {

	LineChart<Float> chart;
	
	@Before
	public void setUp() throws Exception {
		chart = ChartFactory.createLineChart();
		addData();
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test
	public void testCreation() {
		boolean thrown = false;
		try {
			chart.createChart("test");
			thrown = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			thrown = true;
		}
		assertFalse(thrown);
	}

	private void addData() {
		for (int i=0; i < 100; i++) {
			List<Float> values = randomList(4);
			chart.insertData("key"+i, values);
		}
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
