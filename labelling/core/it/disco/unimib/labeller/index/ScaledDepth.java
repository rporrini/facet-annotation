package it.disco.unimib.labeller.index;

import java.util.HashSet;

public class ScaledDepth{
	
	public double of(Type type) {
		
		double siblingsDepth = (double)siblingsDepth(type, 0, new HashSet<String>());
		double ancestorsDepth = (double)ancestorsDepth(type, 0, new HashSet<String>());
		
		return (ancestorsDepth + 1.0) / (siblingsDepth + ancestorsDepth + 1.0);
	}
	
	private int siblingsDepth(Type type, int currentDepth, HashSet<String> seenSiblings){
		int subDepth = currentDepth;
		if(seenSiblings.contains(type.uri())) return subDepth;
		seenSiblings.add(type.uri());
		for(Type superType : type.subTypes()){
			subDepth = Math.max(subDepth, siblingsDepth(superType, currentDepth + 1, seenSiblings));
		}
		return subDepth;
	}
	
	private int ancestorsDepth(Type t, int currentDepth, HashSet<String> seenAncestors){
		int subDepth = currentDepth;
		if(seenAncestors.contains(t.uri())) return subDepth;
		seenAncestors.add(t.uri());
		for(Type subType : t.superTypes()){
			subDepth = Math.max(subDepth, ancestorsDepth(subType, currentDepth + 1, seenAncestors));
		}
		return subDepth;
	}
}