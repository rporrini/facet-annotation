package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.NTriple;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;


public class TripleBuilder{
	
	private Node s = new BNode("_:1");
	private Node p = new BNode("_:2");
	private Node o = new BNode("_:3");

	public TripleBuilder withSubject(String subject) throws Exception{
		this.s = new Resource(new URI(subject));
		return this;
	}
	
	public TripleBuilder withPredicate(String predicate) throws Exception{
		this.p = new Resource(new URI(predicate));
		return this;
	}
	
	public TripleBuilder withLiteral(String literal){
		this.o = new Literal(literal);
		return this;
	}
	
	public NTriple asTriple(){
		return new NTriple(new Node[]{s, p, o});
	}
	
	public String asNTriple(){
		return StringUtils.join(new Object[]{
				s.toN3(),
				p.toN3(),
				o.toN3(),
				"."
		}, " ");
	}
}