package it.disco.unimib.labeller.index;

import org.apache.commons.lang3.StringUtils;

public class RDFPredicate {
	private String uri;

	public RDFPredicate(String uri){
		this.uri = uri;
	}
	
	public String label() {
		String[] splitted = StringUtils.split(uri, "/#");
		return splitted[splitted.length - 1];
	}
	
	public String uri(){
		return this.uri;
	}

	public String namespace() {
		return uri.replace(label(), "");
	}
}