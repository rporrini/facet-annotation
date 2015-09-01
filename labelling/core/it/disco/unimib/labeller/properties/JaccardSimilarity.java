package it.disco.unimib.labeller.properties;

import java.util.HashSet;

public class JaccardSimilarity{
	
	public double between(TypeDistribution d1, TypeDistribution d2){
		
		HashSet<String> intersection = new HashSet<String>(d1.all());
		intersection.retainAll(d2.all());
		
		HashSet<String> union = new HashSet<String>(d1.all());
		union.addAll(d2.all());
		
		return (double)intersection.size() / (double)union.size();
	}
}