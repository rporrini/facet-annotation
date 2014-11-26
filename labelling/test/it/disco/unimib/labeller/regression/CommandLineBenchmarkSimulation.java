package it.disco.unimib.labeller.regression;

import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.SingleFacet;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.tools.RunEvaluation;
import it.disco.unimib.labeller.tools.TrecResultPredicate;

import java.util.ArrayList;
import java.util.List;

public class CommandLineBenchmarkSimulation{
	
	public List<TrecResultPredicate> run(String knowledgeBase, int groupID) throws Exception {
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
		List<TrecResultPredicate> results = new ArrayList<TrecResultPredicate>();
		for(String line : summary.result().split("\n")){
			results.add(new TrecResultPredicate(line));
		}
		return results;
	}
}