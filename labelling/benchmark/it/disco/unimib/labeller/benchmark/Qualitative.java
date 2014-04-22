package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.List;

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
	public Metric track(GoldStandardGroup group, List<AnnotationResult> results) {
		track(group.domain(), group.context(), group.label(), results.get(0).value());
		return this;
	}

	private void track(String domain, String context, String expected, String value) {
		this.results.add(domain + "|" + context + "|" + expected + "|" + value);
	}
}
