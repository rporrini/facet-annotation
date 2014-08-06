package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FileSystemConnector;

import java.io.File;

public class GetGoldStandardPredicateDistribution {

	public static void main(String[] args) throws Exception {
		String goldStandard = "../evaluation/results/gold-standard.qrels";
		String lastId = "";
		double moreRelevant = 0;
		double lessRelevant = 0;
		for(String line : new FileSystemConnector(new File(goldStandard)).lines()){
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
