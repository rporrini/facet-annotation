package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.CommandLineBenchmarkSimulation;

import org.apache.commons.lang3.StringUtils;

public class RunAlgorithm {

	public static void main(String[] args) throws Exception {
		
//		1668711967,
//		1689442184,
//		1744816435,
//		1802054300,
//		2021450258,
//		2125380335,
//		753388668
		
		CommandLineBenchmarkSimulation simulation = new CommandLineBenchmarkSimulation()
					.onDBPedia()
					.weightedFrequency()
					.annotate(1689442184);
		
		System.out.println(StringUtils.join(simulation.results(),"\n"));
	}
}
