package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Qualitative implements Summary {

	private ArrayList<String> results;

	public Qualitative(){
		this.results = new ArrayList<String>();
		this.results.add("Qualitative analysis");
		track("CONTEXT", "EXPECTED", "ACTUAL");
	}
	
	@Override
	public String result() {
		return StringUtils.join(results, "\n");
	}

	@Override
	public Summary track(GoldStandardGroup group, List<AnnotationResult> results) {
		if(results.size() > 0)
			track(group.context(), group.label(), results.get(0).value());
		else
			track(group.context(), group.label(), "notFound");
		return this;
	}

	private void track(String context, String expected, String value) {
		this.results.add(context + "|" + expected + "|" + value);
	}
}
