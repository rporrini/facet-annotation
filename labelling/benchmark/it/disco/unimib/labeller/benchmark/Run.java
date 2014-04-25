package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.RankByJaccard;
import it.disco.unimib.labeller.index.RankByRelevance;

import java.io.File;
import java.util.HashMap;

public class Run {

	public static void main(String[] args) throws Exception {
		String algorithm = args[0];
		String metricName = args[1];
		String context = args[2];
		
		Summary summary = analysis(algorithm, metricName);
		BenchmarkConfiguration configuration = configurationOf(algorithm, context);
		new Benchmark(configuration.getAlgorithm()).on(goldStandard(), summary);
		System.out.println();
		System.out.println(configuration.name());
		System.out.println(summary.result());
	}

	private static BenchmarkConfiguration configurationOf(String algorithm, String context) throws Exception{
		switch (algorithm) {
		case "majority":
			return new BenchmarkConfiguration("majority").majorityAnnotation(0.05, context(context));
		case "ml-frequency":
			return new BenchmarkConfiguration("ml-frequency").predicateAnnotation(new RankByFrequency(), context(context));
		case "ml-jaccard":
			return new BenchmarkConfiguration("ml-jaccard").predicateAnnotation(new RankByJaccard(), context(context));
		case "ml-tfidf":
			return new BenchmarkConfiguration("ml-tfidf").predicateAnnotation(new RankByRelevance(), context(context)); 
		default:
			return null;
		}
	}
	
	private static FullTextQuery context(String context){
		HashMap<String, FullTextQuery> contexts = new HashMap<String, FullTextQuery>();
		contexts.put("with-context", new MandatoryContext());
		contexts.put("without-context", new OptionalContext());
		return contexts.get(context);
	}

	private static Summary analysis(String algorithm, String name){
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
		summaries.put("qualitative", new Qualitative());
		summaries.put("questionnaire", new Questionnaire());
		summaries.put("trec", new TrecEval(algorithm));
		return summaries.get(name);
	}
	
	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard"))).getGroups();
	}
}
