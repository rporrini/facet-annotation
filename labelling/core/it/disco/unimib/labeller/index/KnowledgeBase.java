package it.disco.unimib.labeller.index;

public class KnowledgeBase{
	
	private String name;

	public KnowledgeBase(String name){
		this.name = name;
	}
	
	public String label() {
		String field = "label";
		if(this.name.equals("dbpedia")) field = "property";
		return field;
	}
}