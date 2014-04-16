package it.disco.unimib.labeller.benchmark;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Qualitative implements Metric {

	private ArrayList<String> results;

	public Qualitative(){
		this.results = new ArrayList<String>();
		this.results.add("Qualitative analysis");
		track("DOMAIN", "CONTEXT", "EXPECTED", "ACTUAL");
	}
	
	@Override
	public String result() {
		return StringUtils.join(results, "\n");
	}

	@Override
	public Metric track(String domain, String context, String expected, String actual) {
		results.add(domain + "|" + context + "|" + expected + "|" + actual);
		return this;
	}
}
