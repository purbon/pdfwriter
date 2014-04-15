package test.support;

import java.util.Iterator;
import java.util.Random;

public class Keys implements Iterator<String> {

	private String  dateString;
	private int     hour;
	private int     minutes;
	private int     elements;
	private int     units;
	private int     count;
	private boolean hasNext;
	private int     mode;

	public Keys(int elements, int units, String dateString) {
		this.dateString = dateString;
		if (elements < 0 && units < 0) {
			this.hour     = 0;
			this.minutes  = 0;
			this.mode     = 1;
		} else {
			this.elements = elements;
			this.units    = units;
			this.count    = 0;
			this.mode     = 2;
		}
		this.hasNext = true;
	}
	
	public Keys(String dateString) {
		this(-1, -1, dateString);
	}
	
	public boolean hasNext() {
		return hasNext;
	}

	//00:00%3/4/2014
	//jdl08(P)##30/3/2014
	public String next() {
		StringBuilder sb = new StringBuilder();
		if (mode == 1) {
			sb.append(buildTime(hour, minutes));
			sb.append("%");
		} else if (mode == 2) {
			sb.append(buildElementID(elements, units));
			sb.append("##");
		}
		sb.append(dateString);
		updateCounters();
		return sb.toString();
	}

	private String buildElementID(int elements, int units) {
		Random rand = new Random();
		
		StringBuilder sb = new StringBuilder();
		sb.append("ElementID");
		sb.append(rand.nextInt(elements));
		sb.append("(U");
		sb.append(rand.nextInt(units));
		sb.append(")");
		return sb.toString();
	}

	private void updateCounters() {
		if (mode == 1) {
			minutes++;
			if (minutes > 59) {
				minutes = 0;
				hour++;
				if (hour > 23) {
					hasNext = false;
					hour = 0;
				}
			}
		} else if (mode == 2) {
			int total = elements*units;
			hasNext   = count < total;
			count++;
		}
	}
	private String buildTime(int h, int m) {
		String hour = String.valueOf(h);
		String minutes = String.valueOf(m);
		if (hour.length() < 2)
			hour = "0"+hour;
		if (minutes.length() < 2)
			minutes = "0"+minutes;
		return hour+":"+minutes;
	}
	public void remove() {
	
	}	
}