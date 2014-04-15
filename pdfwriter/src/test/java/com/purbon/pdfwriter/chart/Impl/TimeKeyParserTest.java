package com.purbon.pdfwriter.chart.Impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.chart.Impl.KeyParser.KeyParts;
import com.purbon.pdfwriter.chart.Impl.keys.TimeKeyParser;

public class TimeKeyParserTest {

	private TimeKeyParser parser;
	
	@Before
	public void setUp() throws Exception {
		this.parser = new TimeKeyParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOKKeyFormat() {
		String key = "00:00%3/4/2014";
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
		String key = "00:00%3/4/2014";
		KeyParts parts = parser.parse(key);
		assertEquals("00:00", parts.elem);
		assertEquals("3/4/2014", parts.timeString);

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
		String key = "00:00%%3/4/2014";
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
