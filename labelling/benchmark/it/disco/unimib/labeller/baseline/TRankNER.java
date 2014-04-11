package it.disco.unimib.labeller.baseline;

import io.mem0r1es.trank.pipeline.EntityLinking;
import io.mem0r1es.trank.pipeline.NER;
import it.disco.unimib.labeller.labelling.Annotator;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import scala.collection.JavaConversions;
import scala.collection.immutable.Set;

import com.typesafe.config.ConfigFactory;

public class TRankNER implements Annotator {
	
	@Override
	public List<String> annotate(String... instances) throws Exception {
		Set<String> recognizedEntities = NER.runNER(StringUtils.join(instances, ", "));
		Map<URI, String> entities = JavaConversions.asJavaMap(EntityLinking.linkEntities(recognizedEntities, ConfigFactory.load()));
		List<String> result = new ArrayList<String>();
		for(URI entity : entities.keySet()){
			result.add(entity.toString());
		}
		return result;
	}
}
