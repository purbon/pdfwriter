package com.purbon.pdfwriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;

/**
 * A PDFPage that acts as a content/index page for a report.
 * 
 * @author purbon
 */
public class PDFContentPage {
 
	private PDFDocument document;
	private PDFPage page;
	
	private int pageCount;
	private boolean created;

	private class Section {
		private List<PDFPage> pages;
		private String name;
		
		public Section() {
			pages = new ArrayList<PDFPage>();
			name  = "";
		}
		
		public List<PDFPage> getContent() {
			return pages;
		}
	}
	
	private ArrayList<Section> sections;
	private float rowHeight;
	private float padding;
	
	public PDFContentPage(PDFDocument document) {
		 sections = new ArrayList<Section>();
		 sections.add(new Section());
		 rowHeight = 20;
		 padding = 50;
		 this.document = document;
		 
		 page = new PDFPage();
		 page.setHeader(new PageHeader("Content"));
		 page.setDocument(document, false, false);
		 pageCount = 1;
		 
		 this.created = false;
	}
	
	public void create() throws IOException {
		if (alreadyThrere())
			throw new IOException("Content page already there");
	 	page.create();
 			
		PDPageContentStream cs = new PDPageContentStream(document, page, true, false);
		float offset = (page.getWidth()/3);
		float x0 = getLeftX();
		float x1 = (float)(getRightX()-1.5*offset);
		float x2 = getRightX()-50;
		
		page.setYCursor(page.getTopY()-50);
		
		for (Section section : sections) {
			if (!section.name.isEmpty()) {
				scrolldown();
				addContentString(section.name, x0, page.getYCursor(), cs, PDType1Font.HELVETICA_BOLD, 16);
				scrolldown();
			}

			for (PDFPage sectionPage : section.pages) {
				String title     = "";
				String indexText = "";
				if (sectionPage instanceof PDFTitlePage) {
					title     = "Cover Page";
					indexText = "";
				} else {
					if (sectionPage.hasHeader()) {
						title = sectionPage.getHeaderAsString();
					}
					indexText = "n: " + sectionPage.getNPage();
				}
				if (!title.isEmpty()) {
					addContentString(title, x0, page.getYCursor(), cs);

					addContentString(sectionPage.getLegend(), x1,
							page.getYCursor(), cs,
							PDType1Font.HELVETICA_OBLIQUE, 9);
					if (!indexText.isEmpty())
						addContentString(indexText, x2, page.getYCursor(), cs);

					PDFont font = PDType1Font.HELVETICA;
					float textWidth = (font.getStringWidth(indexText) / 1000) * 18;

					addLinkToPage(x0, page.getYCursor(),
							((x2 + textWidth) - x0), sectionPage);
				}
				if (scrolldown()) {
					cs.close();
					cs = new PDPageContentStream(document, page, true, false);
				}
					
 			}
		}
		page.setDocument(document, false, false);
		if (pageCount > document.getNumberOfPages())
			pageCount = document.getNumberOfPages();
		document.insertPage(pageCount, page);
  		cs.close();
  		this.created = true;
	}
	
	private boolean alreadyThrere() {
		return created;
	}

	private boolean scrolldown() {
 		page.moveYCursorBy(-rowHeight);
 		boolean newPage = page.isFull();
  		if (page.isFull()) {
  			attachNewPage();
  		}
  		return newPage;
	}	
	
	private void attachNewPage() {
	 	page.setDocument(document, false, false);
		document.insertPage(pageCount, page);
		
		page = new PDFPage();
		page.setDocument(document, false, false);
		page.setHeader(new PageHeader("Content"));
		page.setYCursor(page.getTopY()-50);
		pageCount++;
 	}
	
	@SuppressWarnings("unused")
	private void addLinkToPage(float x, float y, String text, PDFPage page) throws IOException {
        PDFont font = PDType1Font.HELVETICA;
		float textWidth = (font.getStringWidth( text )/1000) * 18;
        addLinkToPage(x, y, textWidth, page);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addLinkToPage(float x, float y, float width, PDFPage trgPage) throws IOException {
		  List annotations = page.getAnnotations();
          

          PDAnnotationLink txtLink = new PDAnnotationLink();
          
          PDRectangle position = new PDRectangle();
          position.setLowerLeftX(x);
          position.setLowerLeftY(y);  // down a couple of points
          position.setUpperRightX(x+width);
          position.setUpperRightY(y+10);
          txtLink.setRectangle(position);

          // add an action
          PDActionGoTo action = new PDActionGoTo();
          PDPageDestination destination = new PDPageFitDestination();
          destination.setPage(trgPage);
          action.setDestination(destination);
          txtLink.setAction(action);

          annotations.add(txtLink);
	}
	
	
	
	private void addContentString(String content, float x, float y, PDPageContentStream cs, PDFont font, int size) throws IOException {
		cs.beginText(); 
		cs.setFont(font, size);
 		cs.moveTextPositionByAmount(x, y);
		cs.drawString(content);
		cs.endText();
	}
	
	private void addContentString(String content, float x, float y, PDPageContentStream cs) throws IOException {
		addContentString(content, x, y, cs, PDType1Font.HELVETICA, 12);
	}

	
	public  int getCurrentSection() {
		return sections.size();
	}
	
	public  void addPage(PDFPage page) {
		sections.get(getCurrentSection()-1).pages.add(page);
	}
	
	public  void addPage(int pos, PDFPage page) {
 		addPage(getCurrentSection()-1, pos, page);
	}
	
	public  void addPage(int section, int pos, PDFPage page) {
		try {
			int nPage = sections.get(section).pages.get(pos).getNPage();
			sections.get(section).pages.get(pos).setNPage(nPage+1);
		} catch (Exception ex) {
			
		}
		if (sections.get(section).pages.size() < pos)
			sections.get(section).pages.add(page);
		else
			sections.get(section).pages.add(pos, page);
	}
	
	public  void addSection(String name) {
		Section section = new Section();
		section.name = name;
		sections.add(section);
	}
	
	public List<PDFPage> getSection(int id) {
		return sections.get(id).getContent();
	}
	
	public String getLastSectionName() {
		return 	sections.get(getCurrentSection()-1).name;
	}
	
	public  void setSectionName(int section, String name) {
		sections.get(section).name = name;
	}
	
	public int getSectionsCount() {
		return sections.size();
	}
	
	public  int size() {
		return 	sections.get(getCurrentSection()-1).pages.size();
	}

 	public float getLeftX() {
 		return page.getLeftX()+padding;
	}


 	public float getRightX() {
		return page.getRightX()-padding;
	}

 	public float getTopY() {
		return page.getTopY()-padding;
	}

}
