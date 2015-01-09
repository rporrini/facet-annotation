package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyProperty;
import it.disco.unimib.labeller.index.PartiallyContextualizedProperty;

public class PropertyContextSpecificity implements Specificity{

	private Index index;
	private IndexFields indexFields;

	public PropertyContextSpecificity(Index index, IndexFields fields) {
		this.index = index;
		this.indexFields = fields;
	}
	
	@Override
	public double of(ContextualizedValues request) throws Exception {
		double frequencyOfPropertyInDomain = index.count(request, new PartiallyContextualizedProperty(indexFields));
		double frequencyOfProperty = index.count(request, new OnlyProperty(indexFields));
		return frequencyOfPropertyInDomain / frequencyOfProperty;
	}
}