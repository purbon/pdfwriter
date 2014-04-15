package test.support;

import java.util.Iterator;
import java.util.Random;

public class RandomKeys implements Iterator<String> {

	private int count;
	private int max;
	private boolean hasNext;
	private Random rand;
	public RandomKeys(int max) {
		this.hasNext = true;
		this.max = max;
		this.count = 0;
		this.rand = new Random(System.currentTimeMillis());
	}
	
	public boolean hasNext() {
		return hasNext;
	}

	//00:00%3/4/2014
	//jdl08(P)##30/3/2014
	public String next() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < 10; i++) {
			sb.append(rand.nextInt());
		}
		updateCounters();
		return sb.toString();
	}

	
	private void updateCounters() {
		if (count < max)
			hasNext = false;
		count++;
	}
	
	public void remove() {
	
	}	
}