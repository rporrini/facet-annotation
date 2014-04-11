package it.disco.unimib.labeller.baseline;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scala.collection.JavaConversions;
import scala.collection.Seq;

public class RankedMajority implements TRankRanking{
	
	public HashMap<String, Double> evaluateScoresOf(Map<URI, Seq<URI>> types) {
		HashMap<String, Double> scores = new HashMap<String, Double>();
		for(URI entity : types.keySet()){
			List<URI> currentTypes = JavaConversions.asJavaList(types.get(entity));
			for(URI type: currentTypes){
				String typeString = type.toString();
				if(!scores.containsKey(typeString)) scores.put(typeString, 0d);
				scores.put(typeString, scores.get(typeString) + (1d / Math.sqrt(1d + (double)currentTypes.indexOf(type))));
			}
		}
		return scores;
	}
}