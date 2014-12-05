package it.disco.unimib.labeller.index;

import java.util.ArrayList;
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
	
	@Override
	public String toString() {
		return resource.toString();
	}

	public double scaledDepth() {
		double ancestorsDepth = (double)ancestorsDepth(0);
		double siblingsDepth = (double)siblingsDepth(0);
		
		return (ancestorsDepth + 1.0) / (siblingsDepth + ancestorsDepth + 1.0);
	}
	
	private int siblingsDepth(int currentDepth){
		if(subTypes.isEmpty()) return currentDepth;
		int subDepth = currentDepth;
		for(Type type : subTypes){
			subDepth = Math.max(subDepth, type.siblingsDepth(currentDepth + 1));
		}
		return subDepth;
	}
	
	private int ancestorsDepth(int currentDepth){
		if(superTypes.isEmpty()) return currentDepth;
		int subDepth = currentDepth;
		for(Type type : superTypes){
			subDepth = Math.max(subDepth, type.ancestorsDepth(currentDepth + 1));
		}
		return subDepth;
	}
}