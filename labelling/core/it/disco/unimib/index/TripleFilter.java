package it.disco.unimib.index;

import com.hp.hpl.jena.graph.Triple;

public interface TripleFilter {

	public boolean matches(Triple triple);

}