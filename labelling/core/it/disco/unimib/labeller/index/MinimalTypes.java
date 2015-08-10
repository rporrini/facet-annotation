package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;

public class MinimalTypes {

	public Type[] minimize(Type... types) {
		List<Type> minimalTypes = new ArrayList<Type>();
		for(Type type : types){
			boolean typeIsAncestor = false;
			for(Type minimalType : new ArrayList<Type>(minimalTypes)){
				typeIsAncestor = isAncestor(type, minimalType);
				if(typeIsAncestor){
					break;
				}
				if(isAncestor(minimalType, type)){
					minimalTypes.remove(minimalType);
				}
			}
			if(!typeIsAncestor) minimalTypes.add(type);
		}
		return minimalTypes.toArray(new Type[minimalTypes.size()]);
	}

	private boolean isAncestor(Type type, Type minimalType) {
		boolean ancestorWasFound = false;
		for(Type superType : minimalType.superTypes()){
			if(superType.equals(type)){
				ancestorWasFound = true;
				break;
			}
			ancestorWasFound = ancestorWasFound || isAncestor(type, superType);
		}
		return ancestorWasFound;
	}
}
