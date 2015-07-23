package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.SingleFacet;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.properties.CandidateProperties;
import it.disco.unimib.labeller.properties.Distribution;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.store.NIOFSDirectory;


public class ExportVectorForMatchingProperties {

	public static void main(String[] args) throws Exception {
		
		String knowledgeBase = "dbpedia";
		int id = 1058967988;
		
		BenchmarkParameters parameters = RunEvaluation.benchmarkParameters(new String[]{
				"kb=" + knowledgeBase,
				"algorithm=" + "any",
				"occurrences=" + "simple",
				"context=partial",
				"summary=trec"
		});
		String path = parameters.indexPath(knowledgeBase);
		
		SingleFacet goldStandard = new SingleFacet(parameters.goldStandard(), id);
		GoldStandardFacet group = goldStandard.getFacets()[0];
		List<String> elements = group.elements();
		IndexFields fields = new IndexFields(knowledgeBase);
		
		ContextualizedEvidence evidence = new ContextualizedEvidence(new NIOFSDirectory(new File(path)), parameters.occurrences(), fields);
		ContextualizedValues request = new ContextualizedValues(group.context(), elements.toArray(new String[elements.size()]));
		
		Distribution distribution = new CandidateProperties(evidence).forValues(request, parameters.context(fields));
		
		for(String property : distribution.properties()){
			HashMap<String, Double> subjectsDistribution = new HashMap<String, Double>();
			for(String value : distribution.values()){
				Map<String, Double> subjects = distribution.subjectsOf(property, value);
				for(String subject : subjects.keySet()){
					if(subject.contains("/resource/Category:")) continue;
					if(!subjectsDistribution.containsKey(subject)) subjectsDistribution.put(subject, 0.0);
					subjectsDistribution.put(subject, subjectsDistribution.get(subject) + subjects.get(subject));
				}
			}
			System.out.println(subjectsDistribution);
		}
	}
}
