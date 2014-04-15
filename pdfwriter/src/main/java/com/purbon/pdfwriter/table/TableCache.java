package com.purbon.pdfwriter.table;

import java.util.List;
import java.util.Map;

public interface TableCache {

	public Map<String, List<Float>> getAll();
	public List<Float> get(String key);
	public int size();
	
	public void cacheElements(String[] elements);
	
	public void setHeaders(String[] headers);
	public String[] getHeaders();
	public List<String> keySet();
	
}
