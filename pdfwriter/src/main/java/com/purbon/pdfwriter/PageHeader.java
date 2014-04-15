package com.purbon.pdfwriter;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * A Header for a PDF page.
 * 
 * @author purbon
 * 
 */
public class PageHeader {
	
	private int height;
	private String title;
		
	public PageHeader(String title) {
		this(title, 17);
	}
	public PageHeader(String title, int height) {
		this.title  = title;
		this.height = height;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
		
	public void create(PDDocument document, PDFPage page) throws IOException {
 
		PDPageContentStream cs = new PDPageContentStream(document, page, true, false);
	       
		PDRectangle pageSize = page.getBleedBox();
        float pageWidth = pageSize.getWidth();
        page.translateAndRotateIfNecessary(cs, pageWidth, 0);
        
		cs.beginText();
		PDFont font = PDType1Font.HELVETICA_BOLD;
		cs.setFont(font, 20);
		cs.moveTextPositionByAmount(page.getLeftX(), page.getTopY());
		cs.drawString(title);
		cs.endText();
		cs.drawLine(page.getLeftX(), page.getTopY()-height, page.getRightX(), page.getTopY()-height);
 		cs.close();
	}
	
	public int getHeight() {
		return height+20;
	}
	public String getContentAsString() {
		return title;
	}

}
