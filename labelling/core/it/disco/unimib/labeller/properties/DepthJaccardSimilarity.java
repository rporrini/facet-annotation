package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.ScaledDepths;

import java.util.HashSet;

public class DepthJaccardSimilarity{
	
	private ScaledDepths depths;

	public DepthJaccardSimilarity(ScaledDepths depths){
		this.depths = depths;
	}
	
	public double between(TypeDistribution d1, TypeDistribution d2){
		HashSet<String> intersection = new HashSet<String>(d1.all());
		intersection.retainAll(d2.all());
		
		HashSet<String> union = new HashSet<String>(d1.all());
		union.addAll(d2.all());
		
		return count(intersection) / count(union);
	}
	
	public double count(HashSet<String> types){
		double result = 0.0;
		for(String type : types){
			result += this.depths.of(type);
		}
		return result;
	}
}