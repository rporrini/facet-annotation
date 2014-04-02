package it.disco.unimib.benchmark;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Qualitative implements Metric {

	private ArrayList<String> results;

	public Qualitative(){
		this.results = new ArrayList<String>();
		this.results.add("EXPECTED|ACTUAL");
	}
	
	@Override
	public String result() {
		return StringUtils.join(results, "\n");
	}

	@Override
	public Metric track(String expected, String actual) {
		results.add(expected + "|" + actual);
		return this;
	}
}
