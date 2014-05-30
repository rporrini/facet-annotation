package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.WeightedPredicates;

import java.io.File;
import java.util.HashMap;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class Run {

	public static void main(String[] args) throws Exception {
		
		BenchmarkParameters parameters = new BenchmarkParameters(args);
		
		Summary summary = analysis(parameters);
		BenchmarkConfiguration configuration = configurationOf(parameters);
		new Benchmark(configuration.getAlgorithm()).on(goldStandard(), summary);
		System.out.println();
		System.out.println(configuration.name());
		System.out.println(summary.result());
	}

	private static BenchmarkConfiguration configurationOf(BenchmarkParameters parameters) throws Exception{
		HashMap<String, BenchmarkConfiguration> configurations = new HashMap<String, BenchmarkConfiguration>();
		configurations.put("majority", new BenchmarkConfiguration("majority").majorityAnnotation(parameters.majorityK(), context(parameters.context())));
		configurations.put("ml-frequency", new BenchmarkConfiguration("ml-frequency").predicateAnnotationWithCustomGrouping(new CountPredicates(), context(parameters.context())));
		configurations.put("ml-jaccard", new BenchmarkConfiguration("ml-jaccard").predicateAnnotationWithCustomGrouping(new WeightedPredicates(new JaccardSimilarity()), context(parameters.context())));
		return configurations.get(parameters.algorithm());
	}

	private static FullTextQuery context(String context){
		HashMap<String, FullTextQuery> contexts = new HashMap<String, FullTextQuery>();
		contexts.put("with-context", new MandatoryContext());
		contexts.put("without-context", new OptionalContext());
		return contexts.get(context);
	}

	private static Summary analysis(BenchmarkParameters parameters){
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
		summaries.put("qualitative", new Qualitative());
		summaries.put("questionnaire", new Questionnaire());
		summaries.put("trec", new TrecEval(parameters.algorithm()));
		return summaries.get(parameters.metricName());
	}
	
	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard-enhanced"))).getGroups();
	}
}
