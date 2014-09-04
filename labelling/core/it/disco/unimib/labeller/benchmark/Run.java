package it.disco.unimib.labeller.benchmark;


public class Run {

	public static void main(String[] args) throws Exception {
		Command command = new Command().withArgument("kb", "the knowledge base to use, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
					 .withArgument("algorithm", "the algorithm to use, namely mh, mhw, mhsw, mhwv, mhwcv, ml")
					 .withArgument("occurrences", "the function applied to count each occurrence, namely simple, contextualized")
					 .withArgument("summary", "the format of the results, namely questionnaire, trec");
		try{
			command.parse(args);
		}catch(Exception e){
			System.err.println(command.explainArguments());
			return;
		}
		
		BenchmarkParameters parameters = new BenchmarkParameters(args);
		Summary summary = parameters.analysis();
		new Benchmark(parameters.algorithm()).on(parameters.goldStandard().getGroups(), summary);
		System.out.println(summary.result());
	}
}
