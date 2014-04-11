package it.disco.unimib.test;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;

public class TripleBuilder{
	
	private Node subject;
	private Node predicate;
	private Node object;

	public TripleBuilder withSubject(String subject){
		this.subject = NodeFactory.createURI(subject);
		return this;
	}
	
	public TripleBuilder withPredicate(String predicate){
		this.predicate = NodeFactory.createURI(predicate);
		return this;
	}
	
	public TripleBuilder withLiteral(String literal){
		this.object = NodeFactory.createLiteral(literal);
		return this;
	}
	
	public Triple asTriple(){
		return new Triple(subject, predicate, object);
	}
	
	public String asNTriple(){
		return StringUtils.join(new Object[]{
				asTriple().getSubject(),
				asTriple().getPredicate(),
				asTriple().getObject(),
				"."
		}, " ");
	}
}