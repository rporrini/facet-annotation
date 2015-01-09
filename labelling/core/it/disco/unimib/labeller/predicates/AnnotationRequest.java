package it.disco.unimib.labeller.predicates;


public class AnnotationRequest {
	
	private String context;
	private String[] elements;

	public AnnotationRequest(String context, String[] elements) {
		this.context = context;
		this.elements = elements;
	}
	
	public String context(){
		return context;
	}
	
	public String[] elements(){
		return elements;
	}
}