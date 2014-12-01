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
	
	public void addSuperType(Type superType){
		this.superTypes.add(superType);
	}
	
	public void addSubType(Type subType){
		this.subTypes.add(subType);
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
}