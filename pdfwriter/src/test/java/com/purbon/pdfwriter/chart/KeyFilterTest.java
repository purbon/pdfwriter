package com.purbon.pdfwriter.chart;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.purbon.pdfwriter.table.KeyFilter;


public class KeyFilterTest {

	private KeyFilter filter;
	
	@Before
	public void setUp() throws Exception {
		filter = new KeyFilter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFilterString() {
		String exp = "(\\d\\d:\\d\\d)";
		filter.setRegexp(exp);
		String value = "WindDirection%10:00";
		assertEquals("10:00", filter.apply(value));
	}

}
