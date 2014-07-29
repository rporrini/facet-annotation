package it.disco.unimib.labeller.index;

public class KnowledgeBase{
	
	private String name;
	private IndexFields fields;

	public KnowledgeBase(String name, IndexFields fields){
		this.name = name;
		this.fields = fields;
	}
	
	public String predicateField() {
		String field = fields.label();
		if(this.name.equals("dbpedia")) field = fields.property();
		return field;
	}
}