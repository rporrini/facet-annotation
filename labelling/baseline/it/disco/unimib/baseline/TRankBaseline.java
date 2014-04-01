package it.disco.unimib.baseline;

import io.mem0r1es.trank.pipeline.TypeRanking;
import io.mem0r1es.trank.pipeline.TypeRetrieval;
import io.mem0r1es.trank.ranking.ANCESTORS;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scala.collection.JavaConversions;
import scala.collection.Seq;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TRankBaseline {

	public List<String> typesOf(String... entity) throws URISyntaxException {
		List<URI> entities = new ArrayList<URI>();
		for(String e : entity){
			entities.add(new URI(e));
		}
		Map<URI, Seq<URI>> types = rankTypes(entities);
		List<String> retrievedTypes = new ArrayList<String>();
		for(URI uri : types.keySet()){
			for(URI type: JavaConversions.asJavaCollection(types.get(uri))){
				retrievedTypes.add(type.toString());
			}
		}
		return retrievedTypes;
	}

	private Map<URI, Seq<URI>> rankTypes(List<URI> entities) {
		scala.collection.immutable.Set<URI> set = JavaConversions.asScalaBuffer(entities).toSet();
		Config config = ConfigFactory.load();
		return JavaConversions.asJavaMap(TypeRanking.rankTypes(TypeRetrieval.retrieveTypes(set, config), new ANCESTORS(), config));
	}
}
