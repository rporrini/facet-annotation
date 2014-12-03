package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
								.withArgument("gs", "path of the gold standard")
								.parse(args);

		String algorithm1 = command.argumentAsString("alg1");
		String algorithm2 = command.argumentAsString("alg2");
		String metric = command.argumentAsString("m");
		String goldStandard = command.argumentAsString("gs");
		int k = Integer.parseInt(command.argumentAsString("k"));
		
		double[] algorithm1Results = run(algorithm1, metric, goldStandard, k);
		double[] algorithm2Results = run(algorithm2, metric, goldStandard, k);
		
		// paired two-tailed test
		NumberFormat formatter = new DecimalFormat("#0.0000000000");
		System.out.println(formatter.format(new TTest().pairedTTest(algorithm1Results, algorithm2Results)));
	}

	private static double[] run(String algorithm, String metric, String goldStandard, int k) throws Exception {
		List<String> algorithm1Results = TrecEval.singleAndSummary(k, goldStandard, algorithm, metric);
		double[] algorithm1Values = new double[algorithm1Results.size() - 1];
		for(int i = 0; i<algorithm1Results.size()-1; i++){
			double value = Double.parseDouble(StringUtils.split(algorithm1Results.get(i), "\t")[2]);
			algorithm1Values[i] = value;
		}
		String lastLine = algorithm1Results.get(algorithm1Results.size()-1);
		System.out.println(Double.parseDouble(StringUtils.split(lastLine, "\t")[2]));
		return algorithm1Values;
	}
}
