package com.purbon.pdfwriter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.DefaultPDFWriter;
import com.purbon.pdfwriter.PageHeader;
import com.purbon.pdfwriter.stream.StreamEvent;

public class DefaultPDFWriterTest {

	DefaultPDFWriter writer;
	
	@Before
	public void setUp() throws Exception {
		writer = new DefaultPDFWriter();

	}
	
	@Test
	public void testPrepareReport() {
		File tmpFile = null;
		boolean ioError = false;
		try {
			tmpFile = prepareReport();
			ioError = false;
		} catch (IOException e) {
			ioError = true;
		} finally {
			if (tmpFile != null)
				tmpFile.delete();
		}
		assertFalse(ioError);	
	}
	
	@Test
	public void testPrepareReportWithoutAPage() {
		File tmpFile = null;
		boolean ioError = false;
		try {
			tmpFile = prepareReport(false);
			ioError = false;
		} catch (IOException e) {
			ioError = true;
		} finally {
			if (tmpFile != null)
				tmpFile.delete();
		}
		assertFalse(ioError);	
	}
	
	@Test
	public void testPrepareReportWithTitlePage() {
		File tmpFile = null;
		boolean ioError = false;
		try {
			tmpFile = prepareReport();
			writer.addTitlePage("dummyTitle", "subtitle", new HashMap<String, String>());
			ioError = false;
		} catch (IOException e) {
			ioError = true;
		} finally {
			if (tmpFile != null)
				tmpFile.delete();
		}
		assertFalse(ioError);	
	}
	
	@Test
	public void testPrepareReportWithAPage() {
		File tmpFile = null;
		boolean ioError = false;
		try {
			writer.setPageHeader(new PageHeader("DummyHeader"));
			tmpFile = prepareReport(true);
			ioError = false;
		} catch (IOException e) {
			ioError = true;
		} finally {
			if (tmpFile != null)
				tmpFile.delete();
		}
		assertFalse(ioError);	
	}
	
	@Test
	public void testPrepareReportWithATablePage() {
		File tmpFile = null;
		boolean ioError = false;
		try {
			writer.setPageHeader(new PageHeader("DummyHeader"));
			tmpFile = prepareReport(true);
			
			String[] headers = new String[]{"MIN", "AVG", "MAX"};
			writer.addTable(true, true, headers, "This is a table legend");
			Random rand = new Random(System.currentTimeMillis());
			for(int i=0; i < 10; i++) {
				StreamEvent event = new StreamEvent();
				float min = rand.nextFloat();
				float max = min+rand.nextFloat();
				float avg = min+((max-min)/2);
				event.addElement(String.valueOf(min));
				event.addElement(String.valueOf(avg));
				event.addElement(String.valueOf(max));
				writer.recordSubmited(event);

			}
			writer.flushTableLegend();
			writer.close();
			ioError = false;
		} catch (IOException e) {
			ioError = true;
		} finally {
			if (tmpFile != null)
				tmpFile.delete();
		}
		assertFalse(ioError);	
	}
	
	private File prepareReport() throws IOException {
		return prepareReport(true);
	}
	
	private File prepareReport(boolean addPage) throws IOException {
		File tmpFile = File.createTempFile("report", "pdf");
		if (addPage)
			writer.prepareReport(new FileOutputStream(tmpFile));
		else
			writer.prepareReport(new FileOutputStream(tmpFile), addPage);
		return tmpFile;
	}
}
