package it.disco.unimib.labeller.benchmark;

import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.tools.RunEvaluation;
import it.disco.unimib.labeller.tools.TrecResultPredicate;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class CommandLineBenchmarkSimulation{
	
	private String knowledgeBase;
	private String algorithm;
	private String occurrences;
	private List<TrecResultPredicate> results;
	
	public CommandLineBenchmarkSimulation onDBPedia(){
		this.knowledgeBase = "dbpedia";
		return this;
	}
	
	public CommandLineBenchmarkSimulation onYAGO(){
		this.knowledgeBase = "yago1";
		return this;
	}
	
	public CommandLineBenchmarkSimulation maximumLikelihood(){
		this.algorithm = "ml";
		this.occurrences = "simple";
		return this;
	}
	
	public CommandLineBenchmarkSimulation majority(){
		this.algorithm = "mh";
		this.occurrences = "simple";
		return this;
	}
	
	public CommandLineBenchmarkSimulation weightedFrequency(){
		this.algorithm = "mhw";
		this.occurrences = "contextualized";
		return this;
	}
	
	public CommandLineBenchmarkSimulation annotate(int groupID) throws Exception {
		BenchmarkParameters parameters = RunEvaluation.benchmarkParameters(new String[]{
				"kb=" + knowledgeBase,
				"algorithm=" + algorithm,
				"occurrences=" + occurrences,
				"context=partial",
				"summary=trec"
		});
		Summary summary = parameters.analysis();
		GoldStandard goldStandard = new SingleFacet(parameters.goldStandard(), groupID);
		RunEvaluation.runBenchmark(summary, parameters.algorithm(), goldStandard);
		List<TrecResultPredicate> results = new ArrayList<TrecResultPredicate>();
		for(String line : summary.result().split("\n")){
			results.add(new TrecResultPredicate(line));
		}
		this.results = results;
		return this;
	}
	
	public List<TrecResultPredicate> results(){
		return results;
	}
	
	public CommandLineBenchmarkSimulation assertThatResults(Matcher<? super List<String>> matcher){
		assertThat(resutltsAsStrings(), matcher);
		return this;
	}
	
	private List<String> resutltsAsStrings(){
		List<String> strings = new ArrayList<String>();
		for(TrecResultPredicate predicate : this.results){
			strings.add(predicate.score());
		}
		return strings;
	}
}