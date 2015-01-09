package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.ContextualizedValues;

public class Constant implements Specificity{

	@Override
	public double of(ContextualizedValues request) throws Exception {
		return 1;
	}
}