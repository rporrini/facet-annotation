package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.Constraint;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyProperty;

public class PropertyTypesConditionalEntropy implements Specificity{

	private Index index;
	private IndexFields fields;

	public PropertyTypesConditionalEntropy(Index evidence, IndexFields fields) {
		this.index = evidence;
		this.fields = fields;
	}

	@Override
	public double of(ContextualizedValues request) throws Exception {
		double result = 0.0;
		double totalTriples = index.count(new Constraint().allRecords());
		
		double frequencyOfProperty = index.count(new OnlyProperty(fields).asQuery(request));
		double prior = frequencyOfProperty / totalTriples;
		
		for(String type : request.domainTypes()){
			double frequencyOfPropertyAndType = index.count(new OnlyProperty(fields).asQuery(request).matchExactly(type, fields.subjectType()));
			double joint = frequencyOfPropertyAndType / totalTriples;
			
			double summation = -((joint / prior) * ((Math.log(joint / prior) / Math.log(2))));
			if(Double.isNaN(summation)){
				summation = 0;
			}
			result += summation;
		}
		return result;
	}
}
