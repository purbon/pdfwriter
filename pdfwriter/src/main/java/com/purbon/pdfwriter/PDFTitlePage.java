package com.purbon.pdfwriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

/**
 * A PDFPage that acts as a title page for our reports.
 * 
 * @author purbon
 *
 */
public class PDFTitlePage extends PDFPage {

	private String title, subtitle;
	private Map<String, String> options;
	
	public static final String COVERT_FILE_KEY = "covert.file";
	public static final String PDFCOVERT_FILE_KEY = "pdf.covert.file";

	
	public PDFTitlePage(String title, String subtitle, Map<String, String> options) {
		this.title = title;
		this.subtitle = subtitle;
		this.options = options;
	}
	

	@Override
	public void create() throws IOException {
		int offset = 0;
  		if (options.get(COVERT_FILE_KEY) != null) {
  			String file = options.get(COVERT_FILE_KEY);
  			PDJpeg img = new PDJpeg(document, new FileInputStream(file) );
  			addCovertImage(img);
  		  	options.remove(COVERT_FILE_KEY);
  		  	options.remove(PDFCOVERT_FILE_KEY);
  		}
	  	addTitleAndSubtitle(offset);
  		addFooterReferences();
	}
	
	private void addCovertImage(PDJpeg file) throws IOException {
		PDPageContentStream cs = new PDPageContentStream(document, this, true, false);
		float xPosition = getLeftX()-(borders/2);
		float yPosition = (this.getTopY()/2) - borders - 10;
 		cs.drawImage(file, xPosition, yPosition);
  		cs.close();
	}
	
	private void addTitleAndSubtitle(int offset) throws IOException {
		PDPageContentStream cs = new PDPageContentStream(document, this, true, false);
		cs.beginText();
		cs.setFont(PDType1Font.HELVETICA_BOLD, 30);
		float xPosition = getLeftX()+50;
		float yPosition = ((getTopY()/2)-borders-offset);
		cs.moveTextPositionByAmount(xPosition, yPosition);
		cs.drawString(title);
		cs.endText();
		
		cs.beginText();
 		cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 20);
 		yPosition -= 40;
		cs.moveTextPositionByAmount(xPosition, yPosition);
		cs.drawString(subtitle);
		cs.endText();

  		cs.close();
	}
	
	private void addFooterReferences() throws IOException {
		PDPageContentStream cs = new PDPageContentStream(document, this, true, false);
		float xPosition = getLeftX();
		float yPosition = getLowY()+(20*options.size());
		cs.setFont(PDType1Font.HELVETICA, 12);
		
		for(String key : options.keySet()) {
		  String text = key+" : "+options.get(key);
		  writeText(cs, xPosition, yPosition, text);
		  yPosition -= 20;
		}

  		cs.close();
	}
	
	
	private void writeText(PDPageContentStream cs, float xPosition, float yPosition, String text) throws IOException {
		cs.beginText();
		cs.moveTextPositionByAmount(xPosition, yPosition);
		cs.drawString(text);
		cs.endText();
	}


	public String getTitle() {
		return title;
	}


	public Map<String, String> getOptions() {
		return options;
	}

	
}
