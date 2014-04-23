package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Questionnaire implements Metric {

	private ArrayList<String> results;
	private HashMap<String, String> hyperlinks;
	
	public Questionnaire(){
		this.results = new ArrayList<String>();
		inizializeHyperlinks();
	}
	
	@Override
	public String result() {
		return StringUtils.join(results, "\n");
	}

	@Override
	public Metric track(GoldStandardGroup group, List<AnnotationResult> results) throws Exception {
		trackDomainAndContext(group.domain(), group.context(), group.hyperlink(), group.provider());
		track(concatValues(group.elements()));
		for(AnnotationResult result : results){
			track(result.value());
		}
		return this;
	}

	private void trackDomainAndContext(String domain, String context, String hyperlink, String provider) {
		track("\n" + domain + " (" + context + ")" + hyperlink(provider, hyperlink));
	}
	
	private String hyperlink(String provider, String hyperlink) {
		if(hyperlinks.containsKey(provider)){
			return "|" + hyperlinks.get(provider) + hyperlink;
		}
			
		return "";
	}

	private void track(String value){
		this.results.add(value);
	}
	
	private String concatValues(List<String> elements) {
		return StringUtils.join(elements, " ");
	}
	

	private void inizializeHyperlinks() {
		this.hyperlinks = new HashMap<String, String>();
		this.hyperlinks.put("wikipedia", "http://en.wikipedia.org/wiki/");
		this.hyperlinks.put("amazon", "http://www.amazon.com/gp/search/other/");
		this.hyperlinks.put("discogs", "http://www.discogs.com/");
		this.hyperlinks.put("pricegrabber", "http://www.pricegrabber.com/electronics/tablets-e-readers/p-5908/");
		this.hyperlinks.put("IMDB", "http://www.imdb.com/");
	}
}
