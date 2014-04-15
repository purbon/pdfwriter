package com.purbon.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageNode;

/**
 * PDDocument extension including some new necessary functions that make PDFWriter more easy.
 *  
 * @author purbon
 *
 */
public class PDFDocument extends PDDocument {

	private static Logger log = Logger.getLogger(PDFDocument.class.getName());

	private PDFContentPage contentPage;
	private Map<Integer, PDFPage> dict;
	
	public PDFDocument() throws IOException {
		super();
		contentPage = new PDFContentPage(this);
		this.dict   = new HashMap<Integer, PDFPage>();
 	}
	
	public void addIndexSection(String name) {
		contentPage.addSection(name);
	}
	
	public List<PDFPage> getSection(int id) {
		return contentPage.getSection(id);
	}
	
	public String getLastSection() {
		return contentPage.getLastSectionName();
	}
	
	public int getSectionsCount() {
		return contentPage.getSectionsCount();
	}

	public PDFPage getPage(int pos) {
		return dict.get(pos);
	}
	
	public PDFPage addPage() {
		return addPage(contentPage.size()+1);
	}
	public PDFPage addPage(int pos) {
		return addPage(pos, new PDFPage());
	}
	
	public PDFPage addPage(int pos, PDFPage page) {
		if (pos > contentPage.size()) {
			page.setDocument(this);
			contentPage.addPage(page);
		} else if (pos >= 0){
			page.setDocument(this, false, false);
			contentPage.addPage(pos, page);
			insertPage(pos, page);
		}
		return page;
	}
	
	public PDFPage addPage(int section, int pos, PDFPage page) {
		page.setDocument(this, false, false);
		contentPage.addPage(section, pos, page);
		insertPage(pos, page);
		return page;
	}
	
	public PDFTitlePage addTitlePage(String title, String subtitle) {
		return addTitlePage(title, subtitle, new HashMap<String, String>());
	}
	
	public PDFTitlePage addTitlePage(String title, String subtitle, Map<String, String> options) {
		PDFTitlePage page = new PDFTitlePage(title, subtitle, options);
		page.setDocument(this, true, false);
		contentPage.addPage(0, 1, page);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public void insertPage(int pos, PDFPage page) {
		PDPageNode rootPages = getDocumentCatalog().getPages();
		rootPages.getKids().add(pos, page);
		page.setParent( rootPages );
		rootPages.updateCount();
		page.setNPage(pos);
		dict.put(pos, page);
	}
	
	public void addContentPage() {
		try {
			contentPage.create();
		} catch (IOException e) {
			log.error("Problems while adding the page", e);
		}
	}
	
	public void saveAndClose(OutputStream oStream) throws IOException {
		addContentPage();
		try {
			save(oStream);
		} catch (COSVisitorException e) {
			e.printStackTrace();
		}
		close();
	}

	public void setIndexSectionName(int section, String name) {
		contentPage.setSectionName(section, name);
	}

}
