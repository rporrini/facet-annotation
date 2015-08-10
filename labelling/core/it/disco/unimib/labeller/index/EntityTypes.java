package it.disco.unimib.labeller.index;

import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.OWL;
import org.semanticweb.yars.nx.namespace.RDFS;

public class EntityTypes {

	private TypeHierarchy hierarchy;

	public EntityTypes(TypeHierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Type[] minimize(CandidateProperty... types) {
		Type[] candidateTypes = new Type[types.length];
		if(candidateTypes.length == 0) candidateTypes = asTypes(OWL.THING);
		for(int i=0; i<types.length;i++){
			String uri = types[i].uri();
			Type type = hierarchy.typeFrom(uri);
			if(type == null) type = new Type(new RDFResource(new Resource(uri)));
			candidateTypes[i] = type;
		}
		return new MinimalTypes().minimize(candidateTypes);
	}

	public Type[] minimizeLiteral(CandidateProperty datatype) {
		return asTypes(new Resource(datatype.uri()));
	}
	
	public Type[] minimizeLiteral() {
		return asTypes(RDFS.LITERAL);
	}

	private Type[] asTypes(Resource literal) {
		return new Type[]{new Type(new RDFResource(literal))};
	}
}
