package it.disco.unimib.labeller.regression;

import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.SingleFacet;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.tools.RunEvaluation;
import it.disco.unimib.labeller.tools.TrecResultPredicate;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class CommandLineBenchmarkSimulation{
	
	private String knowledgeBase;
	private List<String> results;
	
	public CommandLineBenchmarkSimulation onDBPedia(){
		this.knowledgeBase = "dbpedia";
		return this;
	}
	
	public CommandLineBenchmarkSimulation onYAGO(){
		this.knowledgeBase = "yago1";
		return this;
	}
	
	public CommandLineBenchmarkSimulation annotate(int groupID) throws Exception {
		BenchmarkParameters parameters = RunEvaluation.benchmarkParameters(new String[]{
				"kb=" + knowledgeBase,
				"algorithm=mhw",
				"occurrences=contextualized",
				"context=partial",
				"summary=trec"
		});
		Summary summary = parameters.analysis();
		GoldStandard goldStandard = new SingleFacet(parameters.goldStandard(), groupID);
		RunEvaluation.runBenchmark(summary, parameters.algorithm(), goldStandard);
		List<String> results = new ArrayList<String>();
		for(String line : summary.result().split("\n")){
			results.add(new TrecResultPredicate(line).value());
		}
		this.results = results;
		return this;
	}
	
	public CommandLineBenchmarkSimulation assertThatResults(Matcher<? super List<String>> matcher){
		assertThat(this.results, matcher);
		return this;
	}
}