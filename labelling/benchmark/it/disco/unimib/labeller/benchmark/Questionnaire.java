package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Questionnaire implements Metric {

	private ArrayList<String> results;
	
	public Questionnaire(){
		this.results = new ArrayList<String>();
	}
	
	@Override
	public String result() {
		return StringUtils.join(results, "\n");
	}

	@Override
	public Metric track(GoldStandardGroup group, List<AnnotationResult> results) throws Exception {
		trackDomainAndContext(group.domain(), group.context());
		track(concatValues(group.elements()));
		for(AnnotationResult result : results){
			track(result.value());
		}
		return this;
	}

	private void trackDomainAndContext(String domain, String context) {
		track("\n" + domain + " " + context);
	}
	
	private void track(String value){
		this.results.add(value);
	}
	


	private String concatValues(List<String> elements) {
		String values = "";
		for(String value : elements){
			values = values.concat(" " + value);
		}
		return values.trim();
	}
}
