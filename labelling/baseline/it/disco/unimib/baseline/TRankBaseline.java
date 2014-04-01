package it.disco.unimib.baseline;

import io.mem0r1es.trank.pipeline.TypeRanking;
import io.mem0r1es.trank.pipeline.TypeRetrieval;
import io.mem0r1es.trank.ranking.ANCESTORS;

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
		HashMap<URI, Double> scores = evaluateScoresOf(rankedTypes(entities));
		double maxScore = -1d;
		URI candidate = null;
		for(URI type : scores.keySet()){
			double score = scores.get(type);
			if(score > maxScore){
				candidate = type;
				maxScore = score;
			}
		}
		return candidate.toString();
	}

	private HashMap<URI, Double> evaluateScoresOf(Map<URI, Seq<URI>> types) {
		HashMap<URI, Double> scores = new HashMap<URI, Double>();
		for(URI uri : types.keySet()){
			List<URI> currentTypes = JavaConversions.asJavaList(types.get(uri));
			for(URI type: currentTypes){
				if(!scores.containsKey(type)) scores.put(type, 0d);
				scores.put(type, scores.get(type) + (1d / Math.sqrt((double)currentTypes.indexOf(type))));
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
