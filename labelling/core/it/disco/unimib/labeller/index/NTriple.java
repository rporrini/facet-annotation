package it.disco.unimib.labeller.index;

import org.semanticweb.yars.nx.Node;

public class NTriple{
	
	private Node[] nodes;

	public NTriple(Node[] triple){
		this.nodes = triple;
	}
	
	public String subject(){
		return nodes[0].toString();
	}
	
	public RDFPredicate predicate(){
		return new RDFPredicate(nodes[1].toString());
	}
	
	public String object(){
		return nodes[2].toString();
	}
}