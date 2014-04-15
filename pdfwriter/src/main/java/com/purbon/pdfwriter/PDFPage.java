package com.purbon.pdfwriter;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * A PDPage extension with some useful addons that makes our work easy.
 * 
 * @author purbon
 *
 */
public class PDFPage extends PDPage {

	protected PDDocument document;
	protected PageHeader header;
	private String     legend;
	protected int borders = 25;
	
	private float yCursor;
	
	private int nPage;
	
	protected PDFPage() {
		super(PAGE_SIZE_A3);
		yCursor = getMediaBox().getUpperRightY()-borders;
		nPage  = -1;
		legend = "";
 	}
	
	public void create() throws IOException {
		if (header != null)
			header.create(document, this);
	}
	
	public void setDocument(PDDocument document) {
		setDocument(document, true, true);
	}

	public void setDocument(PDDocument document, boolean addPage, boolean addPageCount) {
		if (addPage)
			document.addPage(this);
		this.document = document;
		this.nPage = document.getNumberOfPages();
		try {
			if (addPageCount)
				printPageCount();
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}
	
	public String getHeaderAsString() {
		String headerAsString = "";
		if (header != null) {
			headerAsString = header.getContentAsString();
		}
		return headerAsString;
	}
	
	public void setHeader(PageHeader header) {
		this.header = header;
	}
	
	public float getYCursor() {
		return yCursor;
	}
	
	public void moveYCursorBy(float offset) {
		yCursor += offset;
	}
	
	public void setYCursor(float yCursor) {
		this.yCursor = yCursor;
	}


	@SuppressWarnings("unused")
	public float getRightX() {
		try {
			int angle = getRotation();
			PDRectangle box = getMediaBox();
 			return box.getUpperRightY()-borders;
		} catch (Exception ex) {
			return getMediaBox().getUpperRightX()-borders;
		}
	}
	@SuppressWarnings("unused")
	public float getLeftX() {
		try {
			int angle = getRotation();
			PDRectangle box = getMediaBox();
 			return box.getLowerLeftY()+borders;
		} catch (Exception ex) {
			return getMediaBox().getLowerLeftX()+borders;
		}
		
	}
	@SuppressWarnings("unused")
	public float getTopY() {
		try {
			int angle = getRotation();
			PDRectangle box = getMediaBox();
 			return box.getUpperRightX()-borders;
		} catch (Exception ex) {
			return getMediaBox().getUpperRightY()-borders;
		}
	}
	@SuppressWarnings("unused")
	public float getLowY() {
		try {
			int angle = getRotation();
			PDRectangle box = getMediaBox();
 			return box.getLowerLeftX()+borders;
		} catch (Exception ex) {
			return getMediaBox().getLowerLeftY()+borders;
		}
	}

	public float getWidth() {
		return this.getRightX()-this.getLeftX();
	}
	
	public int getNPage() {
		return nPage;
	}
	
	private void printPageCount() throws IOException {
		PDPageContentStream cs = new PDPageContentStream(document, this, true, false);

		cs.beginText();
		cs.setFont(PDType1Font.HELVETICA, 10);
 		cs.moveTextPositionByAmount(getRightX()-100, getLowY());
 		String pageCountString = "Page: "+nPage;
		cs.drawString(pageCountString);
		cs.endText();
  		cs.close();
	}
	
	@SuppressWarnings("unused")
	public void translateAndRotateIfNecessary(PDPageContentStream cs, double dx, double dy) throws IOException {
		try {
			int angle = getRotation();
			double ca = 0; //Math.round(Math.cos(angle));
			double sa = 1; //Math.ceil(Math.sin(angle))
			cs.concatenate2CTM(ca,sa,-sa,ca,dx,dy);
		} catch (Exception ex) {
			
		}
	}

	public boolean isFull() {
 		return getYCursor() <= getLowY();
	}

	public boolean hasHeader() {
 		return header != null && header.getContentAsString() != "";
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}
	
	public String getLegend() {
		return legend;
	}

	public void setNPage(int nPage) {
		this.nPage = nPage;
	}

	public float getHeight() {
		return this.getTopY()-this.getLowY();
	}
 
}
