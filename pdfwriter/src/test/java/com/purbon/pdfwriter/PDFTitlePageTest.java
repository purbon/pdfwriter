package com.purbon.pdfwriter;

import static org.junit.Assert.assertFalse;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.PDFDocument;
import com.purbon.pdfwriter.PDFTitlePage;

public class PDFTitlePageTest {

	PDFDocument doc;
	File coverImage;
	
	@Before
	public void setUp() throws Exception {
		doc = new PDFDocument();
		coverImage  = getDummyImage();

	}
	@After
	public void tearDown() throws Exception {
		if (coverImage != null)
			coverImage.delete();
	}
	
	@Test
	public void testTitleWithProperties() {
		Map<String, String> options = new HashMap<String, String>();
		for(int i=0; i < 5; i++) {
			options.put("Key "+i, "Property "+i);
		}
		PDFTitlePage page = doc.addTitlePage("This is a title", "and this their subtitle", options);
		boolean fired;
		try {
			page.create();
			fired = false;
		} catch (IOException e) {
			fired = true;
		}
		assertFalse(fired);
	}
	
	@Test
	public void testTitlePageWithCovert() {
		Map<String, String> options = new HashMap<String, String>();
		options.put(PDFTitlePage.COVERT_FILE_KEY, coverImage.getAbsolutePath());
		PDFTitlePage page = doc.addTitlePage("This is a title", "and this their subtitle", options);
		boolean fired;
		try {
			page.create();
			fired = false;
		} catch (IOException e) {
			fired = true;
		}
		assertFalse(fired);	
	}
	
	
	private File getDummyImage() throws IOException {
		BufferedImage image =  new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawString("This is a dummy image text", 0, 0);
		File file = new File("dummy.jpeg");
		ImageIO.write(image, "jpeg", file);
		return file;
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
