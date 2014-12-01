package it.disco.unimib.labeller.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.parser.NxParser;

public class TypeHierarchy {

	private HashMap<String, Type> types;

	public TypeHierarchy(InputFile file) throws Exception {
		this.types = new HashMap<String, Type>();
		for(String line : file.lines()){
			NTriple nTriple = new NTriple(NxParser.parseNodes(line));
			RDFResource subType = nTriple.subject();
			RDFResource superType = nTriple.object();
			
			if(!types.containsKey(subType.uri())) types.put(subType.uri(), new Type(subType));
			types.get(subType.uri()).addSuperType(superType);
			
			if(!types.containsKey(superType.uri())) types.put(superType.uri(), new Type(superType));
			types.get(superType.uri()).addSubType(subType);
		}
	}

	public Set<Type> getRootCategories() {
		HashSet<Type> roots = new HashSet<Type>();
		for(String uri : types.keySet()){
			Type type = types.get(uri);
			if(type.isRoot()) roots.add(type);
		}
		return roots;
	}
}
