package it.disco.unimib.labeller.index;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;

public class RDFResource {
	
	private Node node;

	public RDFResource(Node uri){
		this.node = uri;
	}
	
	public String label() {
		String[] splitted = StringUtils.split(node.toString(), "/#");
		return splitted[splitted.length - 1];
	}
	
	public String uri(){
		return this.node.toString();
	}

	public String namespace() {
		return node.toString().replace(label(), "");
	}
	
	public RDFResource datatype() {
		Resource datatype = ((Literal)node).getDatatype();
		if(datatype == null) datatype = new Resource("http://www.w3.org/1999/02/22-rdf-syntax-ns#Literal");
		return new RDFResource(datatype);
	}
	
	@Override
	public String toString() {
		return uri();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((RDFResource)obj).uri().equals(this.uri());
	}

	public boolean isLiteral() {
		return node instanceof Literal;
	}
}