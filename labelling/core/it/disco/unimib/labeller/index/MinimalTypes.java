package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;

public class MinimalTypes {

	public Type[] from(Type... types) {
		List<Type> minimalTypes = new ArrayList<Type>();
		for(Type type : types){
			boolean typeToAdd = true;
			for(Type minimalType : new ArrayList<Type>(minimalTypes)){
				if(isAncestor(type, minimalType)){
					typeToAdd = false;
					break;
				}
				if(isAncestor(minimalType, type)){
					minimalTypes.remove(minimalType);
				}
			}
			if(typeToAdd) minimalTypes.add(type);
		}
		return minimalTypes.toArray(new Type[minimalTypes.size()]);
	}

	private boolean isAncestor(Type type, Type minimalType) {
		for(Type superType : minimalType.superTypes()){
			if(superType.equals(type)){
				return true;
			}
			return isAncestor(type, superType);
		}
		return false;
	}
}
