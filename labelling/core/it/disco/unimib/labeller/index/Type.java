package it.disco.unimib.labeller.index;

import java.util.HashSet;

public class Type{
	
	@SuppressWarnings("unused")
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
}