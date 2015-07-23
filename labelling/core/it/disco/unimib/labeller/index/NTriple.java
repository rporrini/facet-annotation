package it.disco.unimib.labeller.index;

import org.semanticweb.yars.nx.Node;

public class NTriple{
	
	private Node[] nodes;

	public NTriple(Node[] triple){
		this.nodes = triple;
	}
	
	public RDFResource subject(){
		return new RDFResource(nodes[0]);
	}
	
	public RDFResource property(){
		return new RDFResource(nodes[1]);
	}
	
	public RDFResource object(){
		return new RDFResource(nodes[2]);
	}
}