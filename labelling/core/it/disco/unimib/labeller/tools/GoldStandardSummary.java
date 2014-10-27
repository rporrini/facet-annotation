package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GoldStandardSummary {

	public static void main(String[] args) throws Exception {
		
		String qRelsPath = "../evaluation/gold-standards/dbpedia-enhanced-without-numbers.qrels";
		String goldStandardPath = "../evaluation/gold-standards/dbpedia-enhanced";

		UnorderedGroups groups = new UnorderedGroups(new File(goldStandardPath));
		HashMap<String, GoldStandardGroup> names = new HashMap<String, GoldStandardGroup>();
		for(String line : new InputFile(new File(qRelsPath)).lines()){
			GoldStandardGroup group = groups.getGroupById(Integer.parseInt(line.split(" ")[0]));
			names.put(group.name(), group);
		}
		
		HashMap<String, Integer> contexts = new HashMap<String, Integer>();
		HashMap<String, Integer> providers = new HashMap<String, Integer>();
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
		for(GoldStandardGroup goldStandard: names.values()){
			addContent(contexts, goldStandard.context());
			addContent(providers, goldStandard.provider());
			addContent(labels, goldStandard.label());
		}
		
		printAll(providers, "Sources:");
		printAll(contexts, "Category Labels:");
		printAll(labels, "Predicates:");
		System.out.println("Total Facets: " + names.size());
		System.out.println();
		System.out.println("Predicates Distribution");
		predicatesDistribution(qRelsPath, groups);
	}
	
	private static void predicatesDistribution(String goldStandard, UnorderedGroups groups) throws Exception {
		String lastId = "";
		double moreRelevant = 0;
		double lessRelevant = 0;
		System.out.println("ID" + "\t" + "N. of Completely Relevant" + "\t" + "N. of Relevant");
		for(String line : new InputFile(new File(goldStandard)).lines()){
			String[] splitted = line.split(" ");
			String id = splitted[0];
			int value = Integer.parseInt(splitted[3]);
			if(!id.equals(lastId) && !lastId.isEmpty()){
				System.out.println(groups.getGroupById(Integer.parseInt(id)).name() + "\t" + moreRelevant + "\t" + lessRelevant);
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

	private static void printAll(HashMap<String, Integer> list, String label) {
		ArrayList<String> keys = new ArrayList<String>(list.keySet());
		Collections.sort(keys);
		System.out.println(label + "(" + keys.size() + ")");
		for(String key : keys){
			System.out.print("\t" + key + " (" + list.get(key) + ")");
			System.out.println();
		}
		System.out.println();
	}

	private static void addContent(HashMap<String, Integer> list, String content) {
		if(!list.containsKey(content)) list.put(content, 0);
		list.put(content, list.get(content)+1);
	}
}
