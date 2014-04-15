package com.purbon.pdfwriter.stream;

import java.util.ArrayList;

public class StreamEvent {

	private ArrayList<String> dataArray;
	
	public boolean addToResumeTable;
	
	public StreamEvent() {
		setDataArray(new ArrayList<String>());
		addToResumeTable = false;
	}

	public ArrayList<String> getDataArray() {
		return dataArray;
	}

	public void setDataArray(ArrayList<String> dataArray) {
		this.dataArray = dataArray;
	}
	
	public void addElement(String element) {
		addElement("", element);
	}
	
	public void addElement(String key, String element) {
		this.dataArray.add(element);
	}

	public void setElement(int i, String element) {
		this.dataArray.set(i, element);
	}

	public boolean isAddToResumeTable() {
		return addToResumeTable;
	}

	public void setAddToResumeTable(boolean addToResumeTable) {
		this.addToResumeTable = addToResumeTable;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < dataArray.size(); i++) {
			String r = dataArray.get(i);
			sb.append(r);
			sb.append(" #");
		}
		return sb.toString();
	}
	
	
	
	
}
