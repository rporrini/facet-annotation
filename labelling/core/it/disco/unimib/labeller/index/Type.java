package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Type{
	
	private RDFResource resource;
	private List<Type> superTypes;
	private List<Type> subTypes;

	public Type(RDFResource resource){
		this.resource = resource;
		this.superTypes = new ArrayList<Type>();
		this.subTypes = new ArrayList<Type>();
	}
	
	public Type addSuperType(Type superType){
		this.superTypes.add(superType);
		return this;
	}
	
	public Type addSubType(Type subType){
		this.subTypes.add(subType);
		return this;
	}
	
	public boolean isRoot(){
		return superTypes.isEmpty();
	}
	
	public List<Type> subTypes(){
		return this.subTypes;
	}
	
	public List<Type> superTypes(){
		return this.superTypes;
	}
	
	@Override
	public String toString() {
		return resource.toString();
	}

	public double scaledDepth() {
		double siblingsDepth = (double)siblingsDepth(0, new HashSet<String>());
		double ancestorsDepth = (double)ancestorsDepth(0, new HashSet<String>());
		
		return (ancestorsDepth + 1.0) / (siblingsDepth + ancestorsDepth + 1.0);
	}
	
	private int siblingsDepth(int currentDepth, HashSet<String> seenSiblings){
		int subDepth = currentDepth;
		if(seenSiblings.contains(this.resource.uri())) return subDepth;
		seenSiblings.add(this.resource.uri());
		for(Type type : subTypes){
			subDepth = Math.max(subDepth, type.siblingsDepth(currentDepth + 1, seenSiblings));
		}
		return subDepth;
	}
	
	private int ancestorsDepth(int currentDepth, HashSet<String> seenAncestors){
		int subDepth = currentDepth;
		if(seenAncestors.contains(this.resource.uri())) return subDepth;
		seenAncestors.add(this.resource.uri());
		for(Type type : superTypes){
			subDepth = Math.max(subDepth, type.ancestorsDepth(currentDepth + 1, seenAncestors));
		}
		return subDepth;
	}
}