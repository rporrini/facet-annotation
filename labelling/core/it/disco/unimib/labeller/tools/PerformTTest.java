package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.inference.TTest;

public class PerformTTest {

	public static void main(String[] args) throws Exception {
		Command command = new Command()
								.withArgument("m", "the considered metric, from trec_eval")
								.withArgument("alg1", "the path of the results of the first algorithm")
								.withArgument("alg2", "the path of the results of the second algorithm")
								.withArgument("k", "restricts the analysis to the top k results")
								.withArgument("gs", "toggle in dept analysis");
		try{
			command.parse(args);
		}catch(Exception e){
			System.err.println(command.explainArguments());
		return;
		}
		
		String algorithm1 = command.argumentAsString("alg1");
		String algorithm2 = command.argumentAsString("alg2");
		String metric = command.argumentAsString("m");
		String goldStandard = command.argumentAsString("gs");
		int k = Integer.parseInt(command.argumentAsString("k"));
		
		double[] algorithm1Results = run(algorithm1, metric, goldStandard, k);
		double[] algorithm2Results = run(algorithm2, metric, goldStandard, k);
		
		// paired two-tailed test
		System.out.println(new TTest().pairedTTest(algorithm1Results, algorithm2Results));
	}

	private static double[] run(String algorithm1, String metric, String goldStandard, int k) throws Exception {
		List<String> algorithm1Results = TrecEval.run(k, goldStandard, algorithm1, metric);
		double[] algorithm1Values = new double[algorithm1Results.size()];
		for(int i = 0; i<algorithm1Results.size(); i++){
			double value = Double.parseDouble(StringUtils.split(algorithm1Results.get(i), "\t")[2]);
			algorithm1Values[i] = value;
		}
		return algorithm1Values;
	}
}
