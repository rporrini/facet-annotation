package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class GoldStandardSummary {

	public static void main(String[] args) throws Exception {
		
		String qRelsPath = "../evaluation/gold-standards/yago1-simple.qrels";
		String goldStandardPath = "../evaluation/gold-standards/yago1-simple";

		UnorderedFacets groups = new UnorderedFacets(new File(goldStandardPath));
		HashMap<String, GoldStandardFacet> names = new HashMap<String, GoldStandardFacet>();
		for(String line : new InputFile(new File(qRelsPath)).lines()){
			GoldStandardFacet group = groups.getGroupById(Integer.parseInt(line.split(" ")[0]));
			names.put(group.name(), group);
		}
		
		HashMap<String, Integer> contexts = new HashMap<String, Integer>();
		HashMap<String, Integer> providers = new HashMap<String, Integer>();
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
		for(GoldStandardFacet goldStandard: names.values()){
			addContent(contexts, goldStandard.context());
			addContent(providers, goldStandard.provider());
			addContent(labels, goldStandard.label());
		}
		
		printAll(providers, "Sources:");
		printAll(contexts, "Category Labels:");
		printAll(labels, "Properties:");
		System.out.println("Total Facets: " + names.size());
		System.out.println();
		System.out.println("Properties Distribution");
		propertiesDistribution(qRelsPath, groups);
	}
	
	private static void propertiesDistribution(String goldStandard, UnorderedFacets groups) throws Exception {
		String lastId = "";
		double moreRelevant = 0;
		double lessRelevant = 0;
		DescriptiveStatistics fullyRelevants = new DescriptiveStatistics();
		DescriptiveStatistics lessRelevants = new DescriptiveStatistics();
		
		System.out.println("ID" + "\t" + "N. of Completely Relevant" + "\t" + "N. of Relevant");
		for(String line : new InputFile(new File(goldStandard)).lines()){
			String[] splitted = line.split(" ");
			String id = splitted[0];
			int value = Integer.parseInt(splitted[3]);
			if(!id.equals(lastId) && !lastId.isEmpty()){
				System.out.println(groups.getGroupById(Integer.parseInt(id)).name() + "\t" + moreRelevant + "\t" + lessRelevant);
				fullyRelevants.addValue(moreRelevant);
				moreRelevant = 0;
				lessRelevants.addValue(lessRelevant);
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
		System.out.println("--------------------------------------");
		printStats(fullyRelevants, "Completely Relevant Properties");
		printStats(lessRelevants, "Relevant Properties");
	}

	private static void printStats(DescriptiveStatistics fullyRelevants,
			String label) {
		System.out.println(label + ":");
		System.out.println("\tAverage: " + fullyRelevants.getMean());
		System.out.println("\tVariance: " + fullyRelevants.getVariance());
		System.out.println("\tStd Dev: " + fullyRelevants.getStandardDeviation());
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
