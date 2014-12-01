package it.disco.unimib.labeller.index;

import java.util.HashSet;

public class Type{
	
	private RDFResource resource;
	private HashSet<RDFResource> superTypes;
	private HashSet<RDFResource> subTypes;

	public Type(RDFResource resource){
		this.resource = resource;
		this.superTypes = new HashSet<RDFResource>();
		this.subTypes = new HashSet<RDFResource>();
	}
	
	public void addSuperType(RDFResource superType){
		this.superTypes.add(superType);
	}
	
	public void addSubType(RDFResource subType){
		this.subTypes.add(subType);
	}
	
	public boolean isRoot(){
		return superTypes.isEmpty();
	}
	
	@Override
	public String toString() {
		return resource.toString();
	}
}