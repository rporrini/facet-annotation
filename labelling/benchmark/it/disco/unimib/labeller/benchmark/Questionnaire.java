package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.net.URLEncoder;
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
		System.out.println(StringUtils.join(results, "\n"));
		return StringUtils.join(results, "\n");
	}

	@Override
	public Metric track(GoldStandardGroup group, List<AnnotationResult> results) throws Exception {
		trackDomainAndContext(group.domain(), group.context(), group.hyperlink(), group.provider());
		trackGroupValues(group);
		trackResults(results);
		return this;
	}

	private void trackDomainAndContext(String domain, String context, String hyperlink, String provider) {
		track("\n" + domain + " (" + context + ")" + hyperlink(provider, hyperlink));
	}
	
	private void trackGroupValues(GoldStandardGroup group) throws Exception {
		ArrayList<String> fiveElements = new ArrayList<String>();
		int size = group.elements().size();
		for(int i = 1; i <= size; i++){
			fiveElements.add(group.elements().get(i-1));
			if(i%5 == 0){
				track(concatValues(fiveElements));
				fiveElements.clear();
			}
			if(i == size && size % 5 != 0)
				track(concatValues(fiveElements));
		}
	}
	
	private void trackResults(List<AnnotationResult> results) throws Exception {
		for(AnnotationResult result : results){
			track(result.value() + "| |" + createQuery(result.value()));
		}
	}
	
	private void track(String value){
		this.results.add(value);
	}
	
	private String createQuery(String value) throws Exception {
		return "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+distinct+%3Fs+%3Fo+where+{%3Fs+%3C" +
			   URLEncoder.encode(value, "UTF-8") +
			   "%3E+%3Fo}+LIMIT+100&format=text%2Fhtml&timeout=30000&debug=on";
	}
	
	private String concatValues(List<String> elements) {
		return StringUtils.join(elements, "|");
	}
	
	private String hyperlink(String provider, String hyperlink) {
		if(hyperlinks.containsKey(provider)){
			return "|" + hyperlinks.get(provider) + hyperlink;
		}
		return "";
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
