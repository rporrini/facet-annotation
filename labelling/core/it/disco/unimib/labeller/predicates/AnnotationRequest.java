package it.disco.unimib.labeller.predicates;

import java.util.List;

public class AnnotationRequest {
	
	private String context;
	private List<String> elements;

	public AnnotationRequest(String context, List<String> elements) {
		this.context = context;
		this.elements = elements;
	}
	
	public String context(){
		return context;
	}
	
	public String[] elements(){
		return elements.toArray(new String[elements.size()]);
	}
}