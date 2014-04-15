package com.purbon.pdfwriter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.purbon.pdfwriter.PDFDocument;
import com.purbon.pdfwriter.PDFPage;
import com.purbon.pdfwriter.PDFTitlePage;
import com.purbon.pdfwriter.PageHeader;

public class PDFDocumentTest {

	PDFDocument doc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		doc = new PDFDocument();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddSection() {
		int toAdd = 10;
		for(int i=0; i < toAdd; i++) {
			doc.addIndexSection("Section"+i);
		}
		assertEquals(toAdd+1, doc.getSectionsCount());
	}
	
	@Test
	public void testAddTitleAndPage() throws IOException {
		doc.addIndexSection("Section01");

		PDFTitlePage titlePage = doc.addTitlePage("title", "sub");
		titlePage.create();

		PDFPage page = doc.addPage();
		page.setHeader(new PageHeader("Page"));
		page.create();

		assertEquals(1, doc.getSection(0).size());
		assertEquals(2, doc.getSectionsCount());

	}
	
	@Test
	public void testAddPages() throws IOException {
		doc.addIndexSection("Section01");
		int toAdd = 10;
		for(int i=0; i < toAdd; i++) {
			PDFPage page = doc.addPage();
			page.setHeader(new PageHeader("Page "+i+" of "+toAdd));
			page.create();
		}
		assertEquals(toAdd, doc.getNumberOfPages());
	}
	
	@Test
	public void testAddALotOfPages() throws IOException {
		doc.addIndexSection("Section01");
		int toAdd = 100;
		for(int i=0; i < toAdd; i++) {
			PDFPage page = doc.addPage();
			page.setHeader(new PageHeader("Page "+i+" of "+toAdd));
			page.create();
		}
		doc.addContentPage();
		assertEquals(toAdd+2, doc.getNumberOfPages());
	}
	
	@Test
	public void testInsertPage() throws IOException {
		doc.addIndexSection("Section01");
		int toAdd = 10;
		for(int i=0; i < toAdd; i++) {
			PDFPage page = doc.addPage();
			page.setHeader(new PageHeader("Page "+i+" of "+toAdd));
			page.create();
		}
		PDFPage page = doc.addPage(3);
		page.setHeader(new PageHeader("Inserted Page"));
		page.create();
		doc.addContentPage();
		assertEquals(toAdd+2, doc.getNumberOfPages());
	}
	
	@Test
	public void testInsertPageIntoSection() throws IOException {
		doc.addIndexSection("Section01");
		int toAdd = 10;
		for(int i=0; i < toAdd; i++) {
			if (i == 4)
				doc.addIndexSection("Section02");
			PDFPage page = doc.addPage();
			page.setHeader(new PageHeader("Page "+i+" of "+toAdd));
			page.create();
		}
		PDFPage page = doc.addPage(1, 3, new PDFPage());
		page.setHeader(new PageHeader("Inserted Page"));
		page.create();
		assertEquals(page.getHeaderAsString(), doc.getSection(1).get(3).getHeaderAsString());
	}
	
	@Test
	public void testGetPage() throws IOException {
		doc.addIndexSection("Section01");
		int toAdd = 10;
		for(int i=0; i < toAdd; i++) {
			PDFPage page = doc.addPage();
			page.setHeader(new PageHeader("Page "+i+" of "+toAdd));
			page.create();
		}
		PDFPage page = doc.addPage(3);
		page.setHeader(new PageHeader("Inserted Page"));
		page.create();
		
		assertEquals(page.getHeaderAsString(), doc.getPage(3).getHeaderAsString());
	}
	
	
	@Test
	public void testSaveAndClose() throws IOException {
		doc.addIndexSection("Section01");
		PDFPage page = doc.addPage();
		page.setHeader(new PageHeader("DummyPage"));
		page.create();
		File f = File.createTempFile("test", "pdf");
		doc.saveAndClose(new FileOutputStream(f));
		assertTrue(f.length() > 0);
		f.deleteOnExit();
	}
	
	@SuppressWarnings("unused")
	private void save(PDFDocument doc) {
		try {
			doc.saveAndClose(new FileOutputStream("test.pdf"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
