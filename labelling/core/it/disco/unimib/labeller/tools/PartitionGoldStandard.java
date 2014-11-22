package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PartitionGoldStandard {

	public static void main(String[] args) throws Exception {
		Command command = new Command()
			.withArgument("kb", "the knowledge base for which the gold standard has to be partitioned, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
			.withArgument("n", "the name to be filtered")
			.withArgument("r", "if true the filter keeps all matching groups, if false keeps all the not matching groups")
			.parse(args);

		String qRels = goldStandardQRels(command.argumentAsString("kb"));
		List<String> filters = command.argumentsAsStrings("n");
		boolean inclusive = Boolean.parseBoolean(command.argumentAsString("r"));
		GoldStandard goldStandard = new BenchmarkParameters(command).goldStandard();
		
		for(String line : new InputFile(new File(qRels)).lines()){
			int id = Integer.parseInt(line.split(" ")[0]);
			GoldStandardFacet group = goldStandard.getGroupById(id);
			if((matches(filters, group) && inclusive) || (!matches(filters, group) && !inclusive)){
				System.out.println(line);
			}
		}
	}

	private static boolean matches(List<String> filters, GoldStandardFacet group) {
		for(String filter : filters){
			if(group.name().contains(filter)){
				return true;
			}
		}
		return false;
	}
	
	private static String goldStandardQRels(String knowledgeBase){
		HashMap<String, String> qrels = new HashMap<String, String>();
		qrels.put("dbpedia", "dbpedia-enhanced.qrels");
		qrels.put("dbpedia-ontology", "dbpedia-enhanced-ontology.qrels");
		qrels.put("dbpedia-with-labels", "dbpedia-with-labels.qrels");
		qrels.put("yago1", "yago1-enhanced.qrels");
		qrels.put("yago1-simple", "yago1-simple.qrels");
		return "../evaluation/gold-standards/" + qrels.get(knowledgeBase);
	}
}
