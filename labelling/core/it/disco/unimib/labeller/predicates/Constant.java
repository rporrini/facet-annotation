package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.ContextualizedValues;

public class Constant implements Specificity{

	@Override
	public double of(ContextualizedValues request) throws Exception {
		return 1;
	}
}