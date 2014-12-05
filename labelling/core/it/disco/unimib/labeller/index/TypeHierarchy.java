package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.semanticweb.yars.nx.parser.NxParser;

public class TypeHierarchy {

	private HashMap<String, Type> types;

	public TypeHierarchy(InputFile file) throws Exception {
		this.types = new HashMap<String, Type>();
		for(String line : file.lines()){
			NTriple nTriple = new NTriple(NxParser.parseNodes(line));
			RDFResource subResource = nTriple.subject();
			RDFResource superResource = nTriple.object();
			
			if(!types.containsKey(subResource.uri())) types.put(subResource.uri(), new Type(subResource));
			if(!types.containsKey(superResource.uri())) types.put(superResource.uri(), new Type(superResource));
			
			Type subType = types.get(subResource.uri());
			Type superType = types.get(superResource.uri());
			
			subType.addSuperType(superType);
			superType.addSubType(subType);
		}
	}
	
	public Type of(String type){
		return types.get(type);
	}

	public List<Type> getRootTypes() {
		List<Type> roots = new ArrayList<Type>();
		for(String uri : types.keySet()){
			Type type = types.get(uri);
			if(type.isRoot()) roots.add(type);
		}
		return roots;
	}
}
