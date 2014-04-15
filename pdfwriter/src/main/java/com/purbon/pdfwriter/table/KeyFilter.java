package com.purbon.pdfwriter.table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyFilter implements Filter {

	private String regexp;
	private Pattern pattern;
	
	public KeyFilter() {
		regexp = "";
	}
	public KeyFilter(String regexp) {
		this.regexp = regexp;
		pattern = Pattern.compile(regexp);
	}
	
	public String apply(String value) {
		if (regexp.isEmpty()) {
			throw new IllegalArgumentException("Missing RegExp"); 
		}
		Matcher matcher = pattern.matcher(value);
		StringBuilder find = new StringBuilder();
		while (matcher.find()) {
			find.append(matcher.group());
		}
		return find.toString();
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
		this.pattern = Pattern.compile(regexp);
	}
}
