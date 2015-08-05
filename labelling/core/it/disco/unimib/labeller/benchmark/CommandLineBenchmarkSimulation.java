package it.disco.unimib.labeller.benchmark;

import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.tools.RunEvaluation;
import it.disco.unimib.labeller.tools.TrecResultProperty;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

public class CommandLineBenchmarkSimulation{
	
	private String knowledgeBase;
	private String algorithm;
	private String occurrences;
	private List<TrecResultProperty> results;
	
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
	
	public CommandLineBenchmarkSimulation domainAndRangeConsistency(){
		this.algorithm = "drc";
		this.occurrences = "contextualized";
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
	
	public CommandLineBenchmarkSimulation weightedFrequencyWithEntropy() {
		this.algorithm = "mhw-e";
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
		List<TrecResultProperty> results = new ArrayList<TrecResultProperty>();
		for(String line : summary.result().split("\n")){
			results.add(new TrecResultProperty(line));
		}
		this.results = results;
		return this;
	}
	
	public List<TrecResultProperty> results(){
		return results;
	}
	
	public CommandLineBenchmarkSimulation assertThatResults(Matcher<? super List<String>> matcher){
		assertThat(resultsAsStrings().toString(), resultsAsStrings(), matcher);
		return this;
	}
	
	private List<String> resultsAsStrings(){
		List<String> strings = new ArrayList<String>();
		for(TrecResultProperty property : this.results){
			strings.add(property.score());
		}
		return strings;
	}
}