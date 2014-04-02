package it.disco.unimib.baseline;

import io.mem0r1es.trank.pipeline.TypeRanking;
import io.mem0r1es.trank.pipeline.TypeRetrieval;
import io.mem0r1es.trank.ranking.ANCESTORS;
import it.disco.unimib.labelling.Events;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scala.collection.JavaConversions;
import scala.collection.Seq;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TRankBaseline {

	public String typeOf(String... entity) throws URISyntaxException {
		List<URI> entities = new ArrayList<URI>();
		for(String e : entity){
			entities.add(new URI(e));
		}
		HashMap<String, Double> scores = evaluateScoresOf(rankedTypes(entities));
		double maxScore = -1d;
		String candidate = null;
		for(String type : scores.keySet()){
			double score = scores.get(type);
			new Events().info(type + ": " + score);
			if(score > maxScore){
				candidate = type;
				maxScore = score;
			}
		}
		return candidate;
	}

	private HashMap<String, Double> evaluateScoresOf(Map<URI, Seq<URI>> types) {
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

	private Map<URI, Seq<URI>> rankedTypes(List<URI> entities) {
		scala.collection.immutable.Set<URI> set = JavaConversions.asScalaBuffer(entities).toSet();
		Config config = ConfigFactory.load();
		return JavaConversions.asJavaMap(TypeRanking.rankTypes(TypeRetrieval.retrieveTypes(set, config), new ANCESTORS(), config));
	}
}
