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
		int id = 1668711967;
		
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
		
		HashMap<String, Double> typeOccurrences = new HashMap<String, Double>();
		HashMap<String, Double> propertyOccurrences = new HashMap<String, Double>();
		HashMap<String, HashMap<String, Double>> globalOccurrences = new HashMap<String, HashMap<String,Double>>();
		for(String property : distribution.properties()){
			HashMap<String, Double> akpOccurrencesDistribution = new HashMap<String, Double>();
			propertyOccurrences.put(property, 0.0);
			for(String value : distribution.values()){
				propertyOccurrences.put(property, propertyOccurrences.get(property) + distribution.scoreOf(property, value));
				Map<String, Double> subjects = distribution.subjectsOf(property, value);
				for(String subject : subjects.keySet()){
					if(subject.contains("/resource/Category:")) continue;
					if(!typeOccurrences.containsKey(subject)) typeOccurrences.put(subject, 0.0);
					typeOccurrences.put(subject, typeOccurrences.get(subject) + subjects.get(subject));
					if(!akpOccurrencesDistribution.containsKey(subject)) akpOccurrencesDistribution.put(subject, 0.0);
					akpOccurrencesDistribution.put(subject, akpOccurrencesDistribution.get(subject) + subjects.get(subject));
				}
			}
			globalOccurrences.put(property, akpOccurrencesDistribution);
		}
		for(String property : new String[]{"http://dbpedia.org/ontology/party"}){
			System.out.println(property);
			for(String subject : globalOccurrences.get(property).keySet()){
				System.out.println(subject + "|" + typeOccurrences.get(subject) + "|" + propertyOccurrences.get(property) + "|" + globalOccurrences.get(property).get(subject));
			}
		}
	}
}
