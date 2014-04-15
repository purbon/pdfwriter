package com.purbon.pdfwriter.chart.Impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.chart.Impl.KeyParser.KeyParts;
import com.purbon.pdfwriter.chart.Impl.keys.SensorUnitKeyParser;

public class SensorUnitKeyParserTest {

	private SensorUnitKeyParser parser;
	
	@Before
	public void setUp() throws Exception {
		this.parser = new SensorUnitKeyParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOKKeyFormat() {
		String key = "sensor(unit)##00/11/2222";
		boolean thrown = false;
		try {
			parser.parse(key);
			thrown = false;
		} catch (IllegalArgumentException ex) {
			thrown = true;
		}
		assertFalse(thrown);
	}
	
	@Test
	public void testOKKey() {
		String key = "sensor(unit)##00/11/2222";
		KeyParts parts = parser.parse(key);
		assertEquals("sensor", parts.elem);
		assertEquals("unit", parts.unit);
		assertEquals("00/11/2222", parts.timeString);

	}
	
	@Test
	public void testNotOKKeyFormat() {
		String key = "notOkKey";
		boolean thrown = false;
		try {
			parser.parse(key);
			thrown = false;
		} catch (IllegalArgumentException ex) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	public void testPartialOKKeyFormat() {
		String key = "sensor(dd)##00/11";
		boolean thrown = false;
		try {
			parser.parse(key);
			thrown = false;
		} catch (IllegalArgumentException ex) {
			thrown = true;
		}
		assertTrue(thrown);
	}
}
