package com.purbon.pdfwriter.stream;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.stream.StreamEvent;

public class StreamEventTest {

	StreamEvent event;
	@Before
	public void setUp() throws Exception {
		event = new StreamEvent();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetElement() {
		for(int i=0; i < 10; i++) {
			String element = "element"+i;
			event.addElement(element);
		}
		event.setElement(0, "newElement");
		assertEquals("newElement", event.getDataArray().get(0));
	}
	
	@Test
	public void testToString() {
		event.addElement("A");
		event.addElement("B");
		assertEquals("A #B #", event.toString());
	}

}
