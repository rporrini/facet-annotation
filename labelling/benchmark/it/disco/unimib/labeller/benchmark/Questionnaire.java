package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Questionnaire implements Metric {

	private ArrayList<String> results;
	
	public Questionnaire(){
		this.results = new ArrayList<String>();
		track("DOMAIN", "CONTEXT");
	}
	
	@Override
	public String result() {
		return StringUtils.join(results, "\n");
	}

	@Override
	public Metric track(GoldStandardGroup group, List<AnnotationResult> results) throws Exception {
		track(group.domain(), group.context());
		track(concatValues(group.elements()));
		track(results.get(0).value());
		return this;
	}

	private String concatValues(List<String> elements) {
		String values = "";
		for(String value : elements){
			values = values.concat(" " + value);
		}
		return values.trim();
	}

	private void track(String domain, String context) {
		this.results.add("\n" + domain + "|" + context);
	}
	
	private void track(String value){
		this.results.add(value);
	}
}
