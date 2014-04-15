package com.purbon.pdfwriter.chart;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.purbon.pdfwriter.PDFDocument;
import com.purbon.pdfwriter.PDFPage;
import com.purbon.pdfwriter.chart.reducer.array.ArrayReducer;
import com.purbon.pdfwriter.chart.reducer.array.NoOpArrayReducer;
import com.purbon.pdfwriter.table.TableCache;

/**
 * Chart to be included within a PDF document
 * @author purbon
 *
 */
public class PDFChart {

	private float rowHeight;
	private PDFDocument document;
	private PDFPage page;
	private float padding; 
	private Chart<Float> chart;
	
	private String yAxisLabel;
	
	private boolean fullPage;
	
	public PDFChart(PDFDocument document, PDFPage page, float startPos) {
		this(document, page, startPos, -1, "");
	}
	
	public PDFChart(PDFDocument document, PDFPage page, float startPos, float padding, String legend) {
		page.setYCursor(startPos);
		this.document = document;
		this.page = page;
		this.rowHeight = 450;
		this.padding = padding;
		this.yAxisLabel = "";
		this.fullPage = false;
 	}
	
	
	public void create() throws IOException {
		create(null);
	}

	public void create(TableCache cache) throws IOException {
		create(cache, new NoOpArrayReducer());
	}
	
	public void create(TableCache cache, ArrayReducer reduce) throws IOException {
		create(cache, new NoOpArrayReducer(), -1, -1, "");
	}
	public void create(TableCache cache, ArrayReducer reducer, int c, int size, String title) throws IOException {
		if (chart == null)
			return;
		
		configureChart(chart, cache);
		if (cache != null) {
			for(String key : cache.keySet()) {
				chart.insertData(key, reducer.reduce(cache.get(key)));
			}
		}
		if (reducer.headers().length > 0)
			chart.setHeaders(reducer.headers());
		
		int width = (int)getWidth();
		int height = 400;
		if (size > 0) {
			width  = (width/2)-10;
		}
		if (isFullPage()) {
			height = (int)getHeight();
		}
		PDJpeg img = build(chart, title, document, width, height);
		writeImageToPDF(img, c, size);
		if (c>0&&(c%2==0))
			scrolldown();
	}
	
	protected void configureChart(Chart<Float> chart, TableCache cache) {
		if (cache != null)
			chart.setHeaders(cache.getHeaders());
		chart.setYAxisLabel(getyAxisLabel());
	}
	
	private PDJpeg build(Chart<Float> chart, String title, PDFDocument document, int width, int height) throws IOException {
        
        JFreeChart obj = null;
        try {
        	obj = chart.createChart(title);
        } catch (Exception ex) {
        	ex.printStackTrace();
        	throw new IOException(ex);
        }
        
     	// output as file
        File outputFile = File.createTempFile("img", "jpg");
        ChartUtilities.saveChartAsJPEG(outputFile, obj, width, height);
       
        PDJpeg img = new PDJpeg(document, new FileInputStream(outputFile) );
        outputFile.delete();
        
		return img;
	}
		
	public void setRowHeight(float rowHeight) {
		this.rowHeight = rowHeight;
	}

	public void setDocument(PDFDocument document) {
		this.document = document;
	}

	public void setPage(PDFPage page) {
		this.page = page;
	}

	public void setPadding(float padding) {
		this.padding = padding;
	}
	
	public String getyAxisLabel() {
		return yAxisLabel;
	}
	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	public void setFullPage(boolean fullPage) {
		this.fullPage = fullPage;
	}
	
	public boolean isFullPage() {
		return fullPage;
	}
	
	public void setChart(Chart<Float> chart) {
		this.chart = chart;
	}
	
	public Chart<Float> getChart() {
		return this.chart;
	}
	
	private void writeImageToPDF(PDJpeg img, int c, int size) throws IOException {
		PDPageContentStream cs = new PDPageContentStream(document, page, true, false);
					
		float x = getLeftX();
		float y = page.getYCursor()-img.getHeight();

		if (c>0&&(c%2==0)) {
			x+=350;
		}
		
		if (size > 0) {
			y = page.getYCursor()-400;
		}
		if (isFullPage())
			  y = page.getLowY();
 		cs.drawImage(img, x, y);
  		cs.close();
	}
	
	private void scrolldown() {
 		page.moveYCursorBy(-rowHeight);
  		if (page.isFull()) {
  			attachNewPage();
  		}
	}	
	
	private void attachNewPage() {
		page = document.addPage();
 	}
	
	private float getLeftX() {
		float x = page.getLeftX();
		if (padding > -1) {
			  x += padding;
		}
		return x;
	}
	
	private float getRightX() {
		float x = page.getRightX();
		if (padding > -1) {
			  x -= padding;
		}
		return x;
	}
	
	private float getWidth() {
		return getRightX()-getLeftX();
	}

	private float getLowY() {
		float x = page.getLowY();
		if (padding > -1) {
			  x += padding;
		}
		return x;
	}
	
	private float getTopY() {
		float x = page.getTopY();
		if (padding > -1) {
			  x -= padding;
		}
		return x;
	}
	
	private float getHeight() {
		return getTopY()-getLowY();
	}

}
