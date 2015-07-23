package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.NTriple;

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
		this.s = new Resource(subject);
		return this;
	}
	
	public TripleBuilder withProperty(String property) throws Exception{
		this.p = new Resource(property);
		return this;
	}
	
	public TripleBuilder withLiteral(String literal){
		this.o = new Literal(literal);
		return this;
	}
	
	public TripleBuilder withTypedLiteral(String literal, String type) {
		this.o = new Literal(literal, new Resource(type));
		return this;
	}
	
	public TripleBuilder withObject(String uri){
		this.o = new Resource(uri);
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