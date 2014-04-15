package com.purbon.pdfwriter.chart.Impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class KeyParser {

	protected class KeyParts {
		 public String elem;
		 public String timeString;
		 public String unit;
		 
		 public KeyParts() {
			 elem = "";
			 timeString = "";
			 unit = "";
		 }
	} 
	
	Pattern pattern;
	
	public KeyParser(String regexp) {
		pattern  = Pattern.compile(regexp);
	
	}
	
	protected abstract KeyParts parseKey(Matcher m);
	
	public KeyParts parse(String key) {
		Matcher m = pattern.matcher(key);
		if (!m.find())
			throw new IllegalArgumentException("Invalid key: "+key);
		return parseKey(m);
	}
	
}
