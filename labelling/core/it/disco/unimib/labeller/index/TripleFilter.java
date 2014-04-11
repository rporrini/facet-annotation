package it.disco.unimib.labeller.index;

import com.hp.hpl.jena.graph.Triple;

public interface TripleFilter {

	public boolean matches(Triple triple);

}