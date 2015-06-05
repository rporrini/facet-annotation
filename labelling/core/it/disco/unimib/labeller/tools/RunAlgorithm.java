package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.CommandLineBenchmarkSimulation;

import org.apache.commons.lang3.StringUtils;

public class RunAlgorithm {

	public static void main(String[] args) throws Exception {
		
		CommandLineBenchmarkSimulation simulation = new CommandLineBenchmarkSimulation()
					.onDBPedia()
					.weightedFrequency()
					.annotate(148489175);
		
		System.out.println(StringUtils.join(simulation.results(),"\n"));
	}
}
