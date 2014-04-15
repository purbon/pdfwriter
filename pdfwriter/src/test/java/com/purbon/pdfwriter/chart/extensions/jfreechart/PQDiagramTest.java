package com.purbon.pdfwriter.chart.extensions.jfreechart;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.purbon.pdfwriter.chart.extensions.jfreechart.PQDiagramm;


public class PQDiagramTest extends TestCase {

	private static final String FILENAME = "PQDiagramm";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPQDiagramm() {
		boolean failed = false;
		try {
			PQDiagramm diagramm = createPQDiagramm("Dummy PQ-Chart", createDataset());
			export(diagramm, 640, 480);
			failed = false;
		} catch (Exception ex) {
			failed = true;
		}
		assertFalse(failed);
	}
	
	private void export(PQDiagramm diagramm, int width, int height) {
		File outputFile = null;
	    try {
	    	outputFile = File.createTempFile(FILENAME, "jpeg");
			ChartUtilities.saveChartAsJPEG(outputFile, diagramm, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputFile != null)
				outputFile.delete();
		}
	}
	
	private XYDataset createDataset() {
		XYSeriesCollection result = new XYSeriesCollection();
		XYSeries series = new XYSeries("P-Q");
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
			XYDataItem item = new XYDataItem(x, y);
			series.add(item);
		}
		result.addSeries(series);
		return result;	
	}
	
	private PQDiagramm createPQDiagramm(String title, XYDataset dataset) {
	
		PQDiagramm chart = PQDiagramm.createScatterPlot(title, "P in MW", "Q in Mvar", dataset, PlotOrientation.VERTICAL, false, false, false);
		
		XYPlot plot = chart.getXYPlot();
		XYItemRenderer xy = plot.getRenderer(); 
		xy.setSeriesPaint(0,new Color(0,0, 255, 100)); 
		xy.setSeriesShape(0, new Ellipse2D.Double(0,0,2,2)); 
		
		return chart;
	}
}
