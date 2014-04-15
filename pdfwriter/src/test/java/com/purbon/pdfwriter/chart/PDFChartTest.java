package com.purbon.pdfwriter.chart;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.PDFDocument;
import com.purbon.pdfwriter.PDFPage;
import com.purbon.pdfwriter.PageHeader;
import com.purbon.pdfwriter.chart.Chart;
import com.purbon.pdfwriter.chart.ChartFactory;
import com.purbon.pdfwriter.chart.PDFChart;
import com.purbon.pdfwriter.table.PDFTableCache;

public class PDFChartTest {

	PDFDocument  doc;
	PDFPage      page;
	PDFChart     pdfChart;
	Chart<Float> chart;
	PDFTableCache cache;
	
	@Before
	public void setUp() throws Exception {
		doc      = new PDFDocument();
		page     = doc.addPage();
		page.setHeader(new PageHeader("Page with diagram"));
		pdfChart = new PDFChart(doc, page, 0);
		pdfChart.setChart(ChartFactory.createBoxChart());
		pdfChart.setDocument(doc);
		pdfChart.setPage(page);
		pdfChart.setRowHeight(10);
		
		cache = new PDFTableCache(4);
		cache.setHeaders(new String[]{"min", "avg", "stddev", "max"});
		Random rand = new Random(System.currentTimeMillis());
		for(int i=0; i < 10; i++) {
			float min = rand.nextFloat();
			float max = min+rand.nextFloat();
			float avg = ((max-min)/2)+min;
			String[] elements = new String[]{String.valueOf(min),String.valueOf(avg),String.valueOf(avg), String.valueOf(max)};
			cache.cacheElements(elements);
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testChartCreation() {
		boolean failed = false;
		try {
			pdfChart.setFullPage(true);
			pdfChart.create(cache);
			page.create();
			failed = false;
		} catch (IOException e) {
			failed = true;
		}
		assertFalse(failed);
	}
}
