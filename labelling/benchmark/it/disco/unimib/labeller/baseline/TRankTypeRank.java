package it.disco.unimib.labeller.baseline;

import io.mem0r1es.trank.pipeline.TypeRanking;
import io.mem0r1es.trank.pipeline.TypeRetrieval;
import io.mem0r1es.trank.ranking.ANCESTORS;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.Events;
import it.disco.unimib.labeller.labelling.TypeRanker;

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

public class TRankTypeRank implements TypeRanker {
	
	private TRankRanking ranking;

	public TRankTypeRank(TRankRanking ranking) {
		this.ranking = ranking;
	}

	@Override
	public AnnotationResult typeOf(String... entity) throws URISyntaxException {
		List<URI> entities = new ArrayList<URI>();
		for(String e : entity){
			entities.add(new URI(e));
		}
		HashMap<String, Double> scores = ranking.evaluateScoresOf(rankedTypes(entities));
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
		return new AnnotationResult(candidate, maxScore);
	}

	private Map<URI, Seq<URI>> rankedTypes(List<URI> entities) {
		scala.collection.immutable.Set<URI> set = JavaConversions.asScalaBuffer(entities).toSet();
		System.setProperty("TRank.index_basepath", "../evaluation/trank-indexes");
		Config config = ConfigFactory.load();
		return JavaConversions.asJavaMap(TypeRanking.rankTypes(TypeRetrieval.retrieveTypes(set, config), new ANCESTORS(), config));
	}
}
