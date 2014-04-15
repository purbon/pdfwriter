package com.purbon.pdfwriter.chart.Impl;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.chart.ChartFactory;
import com.purbon.pdfwriter.chart.Impl.PQChart;

public class PQChartTest {

	PQChart<Float> chart;
	
	@Before
	public void setUp() throws Exception {
		chart = ChartFactory.createPQChart();
		addData();
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test
	public void test() {
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
		Random rand = new Random(System.currentTimeMillis());
		int nKeys = rand.nextInt(300)+1200;
		for (int i=0; i < nKeys; i++) {
			Float x = rand.nextFloat()*rand.nextInt(1000);
			Float y = rand.nextFloat()*rand.nextInt(1000);
			if (rand.nextBoolean()) {
				  x *= -1;
				  if (x < -100) {
					  x = (float) -100;
				  }
			}
			if (rand.nextBoolean()) {
				  y *= -1;
			}
			List<Float> values = new ArrayList<Float>();
			values.add(x);
			values.add(y);
			chart.insertData("key"+i, values);

		}
	}
}
