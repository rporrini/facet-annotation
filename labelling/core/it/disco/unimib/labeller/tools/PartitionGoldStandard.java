package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.HashMap;

public class PartitionGoldStandard {

	public static void main(String[] args) throws Exception {
		Command command = new Command()
			.withArgument("kb", "the knowledge base for which the gold standard has to be partitioned, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
			.withArgument("n", "the name to be filtered");
		try{
			command.parse(args);
		}catch(Exception e){
		System.err.println(command.explainArguments());
			return;
		}
		String qRels = goldStandardQRels(command.argumentAsString("kb"));
		String filter = command.argumentAsString("n");
		GoldStandard goldStandard = new BenchmarkParameters(args).goldStandard();
		
		for(String line : new InputFile(new File(qRels)).lines()){
			int id = Integer.parseInt(line.split(" ")[0]);
			GoldStandardGroup group = goldStandard.getGroupById(id);
			if(group.name().contains(filter)){
				System.out.println(line);
			}
		}
	}
	
	private static String goldStandardQRels(String knowledgeBase){
		HashMap<String, String> qrels = new HashMap<String, String>();
		qrels.put("dbpedia", "dbpedia-enhanced.qrels");
		qrels.put("dbpedia-with-labels", "dbpedia-with-labels.qrels");
		qrels.put("yago1", "yago1-enhanced.qrels");
		qrels.put("yago1-simple", "yago1-simple.qrels");
		return "../evaluation/gold-standards/" + qrels.get(knowledgeBase);
	}
}
