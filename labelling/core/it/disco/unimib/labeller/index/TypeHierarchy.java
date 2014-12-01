package it.disco.unimib.labeller.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.yars.nx.parser.NxParser;

public class TypeHierarchy {

	private HashMap<RDFResource, RDFResource> subTypes;
	private HashSet<RDFResource> categories;

	public TypeHierarchy(InputFile file) throws Exception {
		this.subTypes = new HashMap<RDFResource, RDFResource>();
		this.categories = new HashSet<RDFResource>();
		for(String line : file.lines()){
			NTriple nTriple = new NTriple(NxParser.parseNodes(line));
			RDFResource subType = new RDFResource(nTriple.object());
			RDFResource superType = new RDFResource(nTriple.subject());
			subTypes.put(superType, subType);
			categories.add(superType);
			categories.add(subType);
		}
	}

	public Set<RDFResource> getRootCategories() {
		HashSet<RDFResource> roots = new HashSet<RDFResource>(categories);
		roots.removeAll(subTypes.values());
		return roots;
	}
}
