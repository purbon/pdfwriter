package com.purbon.pdfwriter.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFTableCache implements TableCache {

	private Map<String, List<Float>> tableCache;
	private String[] headers;
	private int ncolumns;
	private ArrayList<String> keySet;
	
	public PDFTableCache(int ncolumns) {
		this.keySet     = new ArrayList<String>();
		this.tableCache = new HashMap<String, List<Float>>();
		this.ncolumns   = ncolumns;
	}
	
	public void cacheElements(String[] elements) {
		 List<Float> cache = new ArrayList<Float>();
		 String key = "General";
		 int    i   = 0;
		 if (elements.length > ncolumns) {
			 key = elements[0];
			 i   = 1;
		 }
		 for(;i < elements.length; i++) {
			 Float value = (Float)Float.parseFloat(elements[i]);
			 cache.add(value);
		 }
		 tableCache.put(key, cache);
		 keySet.add(key);
	}
	
	public List<String> keySet() {
		return keySet;
	}
	
	public int size() {
		return tableCache.size();
	}
	
	public Map<String, List<Float>> getAll() {
		return tableCache;
	}

	public List<Float> get(String key) {
 		return tableCache.get(key);
	}

	public void setHeaders(String[] headers) {
		if (headers.length > ncolumns) {
			headers = Arrays.asList(headers).subList(1, headers.length).toArray(new String[ncolumns]);
		}
		this.headers = headers;
	}
	
	public String[] getHeaders() {
		return headers;
	}

}
