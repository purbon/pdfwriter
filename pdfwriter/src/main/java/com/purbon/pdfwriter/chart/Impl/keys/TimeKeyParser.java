package com.purbon.pdfwriter.chart.Impl.keys;

import java.util.regex.Matcher;

import com.purbon.pdfwriter.chart.Impl.KeyParser;

public class TimeKeyParser extends KeyParser {

	public TimeKeyParser() {
		super("^(\\d{1,2}:\\d{1,2})%(\\d{1,2}\\/\\d{1,2}\\/\\d{4})$");

	}
	//00:00%3/4/2014
	@Override
	public KeyParts parseKey(Matcher m) {
		KeyParts parts = new KeyParts();
		parts.elem = m.group(1);
		parts.timeString = m.group(2);
		parts.unit = parts.elem;
		return parts;
	}

	
}
