package com.purbon.pdfwriter.chart.Impl.keys;

import java.util.regex.Matcher;

import com.purbon.pdfwriter.chart.Impl.KeyParser;

public class SensorUnitKeyParser extends KeyParser {

	public SensorUnitKeyParser() {
		super("^(\\w+)\\((\\w+)\\)##(\\d{1,2}\\/\\d{1,2}\\/\\d{4})$");

	}
	//jdl08(P)##30/3/2014
	@Override
	public KeyParts parseKey(Matcher m) {
		KeyParts parts = new KeyParts();
		parts.elem = m.group(1);
		parts.timeString = m.group(3);
		parts.unit = m.group(2);
		return parts;
	}

	
}
