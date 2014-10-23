package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.index.InputFile;

import java.io.File;

public class ComputeGoldStandardPredicateDistribution {

	public static void main(String[] args) throws Exception {
		String goldStandard = "../evaluation/gold-standards/dbpedia-enhanced.qrels";
		String lastId = "";
		double moreRelevant = 0;
		double lessRelevant = 0;
		for(String line : new InputFile(new File(goldStandard)).lines()){
			String[] splitted = line.split(" ");
			String id = splitted[0];
			int value = Integer.parseInt(splitted[3]);
			if(!id.equals(lastId) && !lastId.isEmpty()){
				System.out.println(id + " " + moreRelevant + " " + lessRelevant);
				moreRelevant = 0;
				lessRelevant = 0;
			}
			if(value == 2){
				moreRelevant++;
			}
			if(value == 1){
				lessRelevant++;
			}
			lastId = id;
		}
	}
}
