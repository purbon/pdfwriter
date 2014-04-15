package com.purbon.pdfwriter.chart.extensions.jfreechart;

import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;


public class PQDiagramm extends JFreeChart {

	private static final long serialVersionUID = 9074403562961740810L;
	
	public PQDiagramm(String title, Font font, Plot plot, boolean legend) {
		super(title, font, plot, legend);	
	}

	public static PQDiagramm createScatterPlot(String title, String xAxisLabel,
			String yAxisLabel, XYDataset dataset, PlotOrientation orientation,
			boolean legend, boolean tooltips, boolean urls) {

		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		
	
		Font font = new Font(JFreeChart.DEFAULT_TITLE_FONT.getFontName(), Font.BOLD, 9);
			
		PQAxis xAxis = new PQAxis(xAxisLabel);
			   xAxis.setLabelFont(font);
		PQAxis yAxis = new PQAxis(yAxisLabel);
			   yAxis.setLabelFont(font);

		xAxis.setaAxis(yAxis);
		xAxis.setE(RectangleEdge.LEFT);
		xAxis.seteL(RectangleEdge.TOP);

		yAxis.setaAxis(xAxis);
		yAxis.setE(RectangleEdge.BOTTOM);
		yAxis.seteL(RectangleEdge.RIGHT);
	
		XYPQPlot plot = new XYPQPlot(dataset, xAxis, yAxis, null);

		XYToolTipGenerator toolTipGenerator = null;
		if (tooltips) {
			toolTipGenerator = new StandardXYToolTipGenerator();
		}

		XYURLGenerator urlGenerator = null;
		if (urls) {
			urlGenerator = new StandardXYURLGenerator();
		}
		XYItemShapeRenderer renderer = new XYItemShapeRenderer();
		renderer.setBaseToolTipGenerator(toolTipGenerator);
		renderer.setURLGenerator(urlGenerator);
		
		plot.setRenderer(renderer);
		plot.setOrientation(orientation);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		

		PQDiagramm chart = new PQDiagramm(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
		return chart;

	}
}
